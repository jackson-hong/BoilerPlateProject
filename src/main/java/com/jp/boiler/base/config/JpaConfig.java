package com.jp.boiler.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    private static final String NAME_DEFAULT_VALUE = "anonymousUser";

    @Bean
    public AuditorAware<String> auditorProvider(){
        return () -> Optional.of(
                Optional.ofNullable(SecurityContextHolder.getContext())
                        .map(SecurityContext::getAuthentication)
                        .map(authentication -> convertAnonymousToAdmin(authentication.getName()))
                        .orElse("ADMIN"));
    }

    private String convertAnonymousToAdmin(String authName){
        // SpringSecurity Default user policy -> anonymousUser
        if(authName.equals(NAME_DEFAULT_VALUE)) return "ADMIN";
        else return authName;
    }
}
