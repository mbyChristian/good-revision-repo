package org.study.subjectresource.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.study.subjectresource.dto.UsersDto;
import org.study.subjectresource.entity.Role;
import org.study.subjectresource.entity.Users;
import org.study.subjectresource.repository.RoleRepo;
import org.study.subjectresource.repository.UsersRepo;
import org.study.subjectresource.service.UsersService;
import org.study.subjectresource.validators.ObjectsValidator;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersServiceImpl implements UsersService {
    private final UsersRepo usersRepo;
    private final RoleRepo roleRepo;
    private final ObjectsValidator<UsersDto> validator;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsersDto save(UsersDto usersDto) {
        validator.validate(usersDto);
        Users users =UsersDto.toEntity(usersDto);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRole(findOrCreateRole());
        Users saved= usersRepo.save(users);
        return UsersDto.toDto(saved);

    }

    @Override
    public UsersDto findById(Long id) {
        Optional<Users> users = usersRepo.findById(id);
        return users.map(UsersDto::toDto).orElseThrow(()-> new EntityNotFoundException("l'utilisateur n'existe pas avec cet id "+id));
    }

    @Override
    public List<UsersDto> findAll() {
        return usersRepo.findAll().stream().map(UsersDto::toDto).collect(Collectors.toList());
    }

    @Override
    public UsersDto update(Long id, UsersDto usersDto) {
        return null;
    }

    @Override
    public void delete(Long id) {
         usersRepo.deleteById(id);
    }

    @Override
    public UsersDto register(@Valid UsersDto usersDto) {
        validator.validate(usersDto);
        if(usersRepo.findByUsername(usersDto.username()).isPresent()) {
            throw new DataIntegrityViolationException("cet utilisateur existe déjà ");
        }

        if(usersRepo.findByEmail(usersDto.email()).isPresent()) {
            throw new DataIntegrityViolationException("cet email existe déjà ");
        }

        Users users = UsersDto.toEntity(usersDto);
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRole(findOrCreateRole());
        Users saved= usersRepo.save(users);
        return UsersDto.toDto(saved);
    }

    @Override
    public Long getTotalUsers(){
        return usersRepo.count();
    }

    @Override
    public void toAdmin(Long userId) {
        // Vérifier si l'utilisateur existe
        Users user = usersRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur introuvable avec l'ID : " + userId));

        // Vérifier si l'utilisateur a déjà le rôle ADMIN
        if ("ROLE_ADMIN".equals(user.getRole().getRoleName())) {
            throw new RuntimeException("L'utilisateur a déjà le rôle ADMIN");
        }

        // Obtenir le rôle ADMIN existant
        Role adminRole = roleRepo.findByRoleName("ROLE_ADMIN")
                .orElseThrow(() -> new RuntimeException("Rôle ADMIN introuvable"));

        // Changer le rôle de l'utilisateur
        user.setRole(adminRole);
        usersRepo.save(user);
    }

    private Role findOrCreateRole() {
        return roleRepo.findByRoleName("ROLE_USER")
                .orElseGet(() -> roleRepo.save(Role.builder().roleName("ROLE_USER").build()));
    }

    public boolean isEmailAvailable(String email) {
        return !usersRepo.existsByEmail(email);
    }
    public boolean isUsernameAvailable(String username) {
        return !usersRepo.existsByUsername(username);
    }

}
