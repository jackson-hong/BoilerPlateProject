package com.jp.boiler.base.common.security.config;

import com.jp.boiler.base.common.filter.jwt.JwtAuthenticationFilter;
import com.jp.boiler.base.common.filter.jwt.JwtAuthorizationFilter;
import com.jp.boiler.base.controller.param.roles.Role;
import com.jp.boiler.base.domain.auth.UserRepository;
import com.jp.boiler.base.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    private final JwtProperties jwtProperties;

    private static final String [] AUTH_WHITELIST = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/configuration/ui",
            "/swagger-resources",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger/**",
            "/h2-console/**",
            "/jp/api/v1/join",
            "/jp/api/v1/login/**"
            ,
    };

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();

        http.headers().frameOptions().disable();

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter) // cors 정책 제외
                .addFilter(jwtAuthenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository, jwtProperties))
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()

                // 테스트용 url
                .antMatchers("/admin-test").hasRole(Role.ROLE_ADMIN.getSecurityRoleValue())
                .antMatchers("/user-test").hasRole(Role.ROLE_USER.getSecurityRoleValue())
                .antMatchers("/manager-test").hasRole(Role.ROLE_MANAGER.getSecurityRoleValue())
                .anyRequest().authenticated()
        ;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtProperties);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager());
        return jwtAuthenticationFilter;
    }
}
