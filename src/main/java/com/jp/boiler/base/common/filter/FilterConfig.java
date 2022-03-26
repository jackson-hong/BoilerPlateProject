package com.jp.boiler.base.common.filter;

import com.jp.boiler.base.common.filter.security.BoilerPlateAuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<BoilerPlateAuthorizationFilter> filterOne(){
        FilterRegistrationBean<BoilerPlateAuthorizationFilter> bean = new FilterRegistrationBean<>(new BoilerPlateAuthorizationFilter());
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
        return bean;
    }
}
