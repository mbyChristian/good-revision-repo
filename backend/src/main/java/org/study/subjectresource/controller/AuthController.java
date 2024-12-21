package org.study.subjectresource.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.study.subjectresource.configuration.JwtUtil;
import org.study.subjectresource.dto.UsersDto;
import org.study.subjectresource.entity.Users;
import org.study.subjectresource.repository.UsersRepo;
import org.study.subjectresource.service.UsersService;
import org.study.subjectresource.service.impl.TokenBlacklistService;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {
    private final UsersService usersService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenBlacklistService tokenBlacklistService;
    private final UsersRepo usersRepo;


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/register")
    public ResponseEntity<UsersDto> register(@RequestBody UsersDto usersDto) {
        return ResponseEntity.ok(usersService.register(usersDto));
    }

    @GetMapping("/checkEmailAvailability")
    public ResponseEntity<Map<String, Boolean>> checkEmailAvailability(@RequestParam String email) {
        boolean isAvailable = usersService.isEmailAvailable(email);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/checkUsernameAvailability")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestParam String username) {
        boolean isAvailable = usersService.isUsernameAvailable(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersDto dto) {
        try {
            String username = dto.username();
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, dto.password())
            );
            if (authentication.isAuthenticated()) {
                //Generation du token
                String token = jwtUtil.generateToken(username);
                Users user =usersRepo.findByUsername(username)
                        .orElseThrow(()-> new UsernameNotFoundException("utilisateur pas trouve"));

                Map<String, Object> authenticationData = new HashMap<>();
                authenticationData.put("token", token);
                authenticationData.put("type", "Bearer");

                Map<String, Object> userData = new HashMap<>();
                userData.put("firstName", user.getFirstName());
                userData.put("lastName", user.getLastName());
                userData.put("role", user.getRole().getRoleName());
                authenticationData.put("user", userData);


                return ResponseEntity.ok(authenticationData);
            }
            return   ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("mot de passe ou nom d'utilisateur invalide");

        } catch (AuthenticationException e) {

            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("mot de passe ou nom d'utilisateur invalide");
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, Authentication authentication) {
        // Vérifier l'authentification
        if (authentication == null || !authentication.isAuthenticated()) {
            return "L'utilisateur n'est pas authentifié";
        }

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return "Token non fourni ou invalide";
        }
        String token = authorizationHeader.substring(7);

        // Extraire la date d'expiration
        Date expiration = jwtUtil.extractExpiration(token);
        long now = System.currentTimeMillis();
        long expireTime = expiration.getTime();

        long ttl = (expireTime - now) / 1000; // TTL en secondes

        if (ttl > 0) {
            tokenBlacklistService.blacklistToken(token, ttl);
            return "Déconnexion réussie. Le token est invalidé jusqu'à son expiration.";
        } else {
            return "Le token est déjà expiré, aucune action nécessaire.";
        }
    }


}
