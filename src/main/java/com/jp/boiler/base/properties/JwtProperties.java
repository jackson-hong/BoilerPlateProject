package com.jp.boiler.base.properties;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "jwt")
@PropertySources({
        @PropertySource("classpath:jwt/jwt-value-dev.properties"),
        @PropertySource(value = "classpath:jwt/jwt-value-${spring.profiles.active}", ignoreResourceNotFound = true)
})
@Getter
@Setter
@RequiredArgsConstructor
@Slf4j
public class JwtProperties {

    private String subject;
    private String algorithm;
    private String coreHeader;
    private String headerType;
    private int refreshTime;
    private Claim claim;

    private static final String BLANK_SPACE = " ";


    @Setter
    @Getter
    @ToString
    public static class Claim{
        private String id;
        private String username;
    }

    public String getCoreHeaderTypeWithBlankSpace(){
        return coreHeader+BLANK_SPACE;
    }
}
