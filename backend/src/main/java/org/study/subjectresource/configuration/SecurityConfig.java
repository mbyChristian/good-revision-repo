package org.study.subjectresource.configuration;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;
import org.study.subjectresource.filter.JwtAuthFilter;
import org.study.subjectresource.service.impl.CustomUserDetailsService;

import static org.study.subjectresource.configuration.CorsConfig.corsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtAuthFilter jwtAuthFilter;



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/sujets/total-sujet").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/total").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/{id}/promote").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/sujets/download/{id}").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/sujets").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/sujets/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/sujets/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/sujets").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/sujets/sujet-telecharges").permitAll()
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Gestion des sessions sans état
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) // Utilisation du bean injecté

                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erreur d'authentification");
                        })
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

