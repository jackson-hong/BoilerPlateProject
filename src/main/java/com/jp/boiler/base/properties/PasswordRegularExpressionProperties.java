package com.jp.boiler.base.properties;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "password")
@PropertySources({
        @PropertySource("classpath:password/pattern/password-pattern-dev.properties"),
        @PropertySource(value = "classpath:password/pattern/password-pattern-${spring.profiles.active}", ignoreResourceNotFound = true)
})
@Getter
@Setter
public class PasswordRegularExpressionProperties {

    private int max;
    private int min;
    private String ruleRegex;
    private String sameAlphabetPattern;
    private String sameSymbolPattern;

}
