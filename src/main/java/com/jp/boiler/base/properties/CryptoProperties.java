package com.jp.boiler.base.properties;

import com.jp.boiler.base.support.crypto.AESSpec;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import javax.annotation.PostConstruct;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "crypto")
@PropertySources({
        @PropertySource("classpath:crypto/crypto-dev.properties"),
        @PropertySource(value = "classpath:crypto/crypto-${spring.profiles.active}", ignoreResourceNotFound = true)
})
@Getter
@RequiredArgsConstructor
@Slf4j
public class CryptoProperties {
    private final List<AESSpec> aesSpecs;
}
