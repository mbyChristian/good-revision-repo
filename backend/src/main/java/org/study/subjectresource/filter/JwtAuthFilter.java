package org.study.subjectresource.filter;


import io.jsonwebtoken.ExpiredJwtException;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.study.subjectresource.configuration.JwtUtil;
import org.study.subjectresource.service.impl.CustomUserDetailsService;
import org.study.subjectresource.service.impl.TokenBlacklistService;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@NonNullApi
public class JwtAuthFilter extends OncePerRequestFilter {
    //    private final UtilisateurRepo utilisateurRepo;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtUtil jwtUtil;
    private static final String AUTHORIZATION = "Authorization";
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader(AUTHORIZATION);
        final String jwt;
        final String username;

        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            jwt = authHeader.substring(7);
            // Vérifier si le token est en blacklist
            if (tokenBlacklistService.isTokenBlacklisted(jwt)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Ce token a été révoqué (déconnecté).");
                return;
            }
            username = jwtUtil.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);



                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Le token JWT a expiré. Veuillez vous reconnecter.");
            return;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Erreur lors de la validation du token JWT.");
            return;
        }

        filterChain.doFilter(request, response);
    }

}
