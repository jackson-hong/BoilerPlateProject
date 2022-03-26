package com.jp.boiler.base.common.security.config.cors;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); // 서버가 응답을 할 때 json 을 자바스크립트에서 처리할 수 있게 할지를 설정함. 위 사항이 false 일 경우 js 등 ajax 요청에서 rest 응답에 대한 데이터를 사용할 수 없음.
        configuration.addAllowedOrigin("*"); // 모든 ip 에 대한 응답 허용
        configuration.addAllowedHeader("*"); // 모든 header 의 응답을 허용함.
        configuration.addAllowedMethod("*"); // 모든 http Method 를 허용함.
        source.registerCorsConfiguration("*",configuration);
        return new CorsFilter(source);
    }
}
