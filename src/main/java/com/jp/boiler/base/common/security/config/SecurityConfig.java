package com.jp.boiler.base.common.security.config;

import com.jp.boiler.base.common.filter.jwt.JwtAuthenticationFilter;
import com.jp.boiler.base.common.filter.jwt.JwtAuthorizationFilter;
import com.jp.boiler.base.common.filter.security.BoilerPlateAuthorizationFilter;
import com.jp.boiler.base.domain.auth.UserRepository;
import io.swagger.models.HttpMethod;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;
    private final UserRepository userRepository;

    private static final String [] AUTH_WHITELIST = {
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/v2/api-docs",
            "/webjars/**",
            "/h2-console/**"
    };

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(AUTH_WHITELIST);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        /*http.addFilterBefore(new BoilerPlateAuthorizationFilter(), SecurityContextPersistenceFilter.class);*/
        http.headers().frameOptions().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter) // cors 정책 제외
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository))
                .formLogin().disable()
                .httpBasic().disable()
                .authorizeRequests()
                .antMatchers("/jp/api/v1/**").permitAll()
                .anyRequest().denyAll();
        ;
    }
}
