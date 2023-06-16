package com.liferay.samples.fbo.bank.accounts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;

@Configuration
@EnableWebSecurity
public class MyCustomSecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            /*
             * This specific method is not protected, it can be called anonymously
             */
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account/api/v1/ping").anonymous()
            )
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/actuator/**").anonymous()
            )
            /*
             * All the other methods of the REST controller are protected
             * A token is required and I've made one specific scope mandatory
             * TODO: create custom scopes in Liferay for the external microservices
             */
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/account/api/v1/**").hasAuthority("SCOPE_Liferay.Headless.Delivery.everything.read")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
            );
        return http.build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new MyAuthoritiesConverter());
        return jwtAuthenticationConverter;
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri(this.jwkSetUri)
            .jwtProcessorCustomizer(customizer -> {
                customizer.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(new JOSEObjectType("at+jwt")));
            })
            .build();
    }

    // See application.yaml for the JWKS URL of Liferay
    @Value("${security.oauth2.resourceserver.jwk.jwk-set-uri}") String jwkSetUri;
}