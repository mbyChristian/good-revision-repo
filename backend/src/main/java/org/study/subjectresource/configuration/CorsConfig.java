package org.study.subjectresource.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorsConfig {

    @Bean
    public static CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Autorise le domaine du frontend
        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE ...
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Applique la config à tous les endpoints
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
