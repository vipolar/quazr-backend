package io.quazr.backend.Security;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwksUri;

    private final AuxiliaryJwtService auxiliaryJwtDecoder;
    private final JwtConverter jwtConverter;

    public SecurityConfig(AuxiliaryJwtService auxiliaryJwtDecoder, JwtConverter jwtConverter) {
        this.auxiliaryJwtDecoder = auxiliaryJwtDecoder;
        this.jwtConverter = jwtConverter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Stateless API
            .authorizeHttpRequests(auth -> auth
                // Endpoints that accept both Guests and Authenticated Users
                .requestMatchers(HttpMethod.GET, "/instances/*/*").permitAll()
                .requestMatchers(HttpMethod.POST, "/instances/*/new").permitAll()
                .requestMatchers(HttpMethod.GET, "/questionnaire/*").permitAll()
                
                // All other endpoints require a valid token
                .anyRequest().authenticated()
            ).oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .decoder(hybridJwtDecoder())
                    .jwtAuthenticationConverter(this.jwtConverter)
                )
            ).sessionManagement(
                session-> session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS)
            );
        
        return http.build();
    }

    @Bean
    public JwtDecoder hybridJwtDecoder() {
        JwtDecoder keycloakDecoder = NimbusJwtDecoder.withJwkSetUri(jwksUri).build();
        return new HybridJwtDecoder(keycloakDecoder, auxiliaryJwtDecoder);
    }
}