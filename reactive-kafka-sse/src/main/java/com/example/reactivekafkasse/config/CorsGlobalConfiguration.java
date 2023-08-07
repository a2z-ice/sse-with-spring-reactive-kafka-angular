package com.example.reactivekafkasse.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
public class CorsGlobalConfiguration implements WebFluxConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {
        corsRegistry.addMapping("/**")
//                .allowedOrigins("http://allowed-origin.com")
                .allowCredentials(true)
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE", "PATCH")
                .allowedHeaders("Origin", "Content-Type", "Accept", "request-type", "secret-key", "cache-control", "postman-token", "content-requestType", "request-requestType", "Content-Type", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Authorization")
//                .maxAge(3600)
        ;
    }
}