package com.jp.boiler.base.common.filter.mdc;

import com.jp.boiler.base.common.annotation.ApiCode;
import com.jp.boiler.base.common.type.MDCKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.jp.boiler.base.common.type.MDCKey.API_CODE;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiCodeFilter extends OncePerRequestFilter {

    private final HandlerMapping handlerMapping;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        if(!ObjectUtils.isEmpty(chain)){
            HandlerMethod handlerMethod = (HandlerMethod) chain.getHandler();
            final ApiCode annotation = handlerMethod.getMethod().getAnnotation(ApiCode.class);
            if(!ObjectUtils.isEmpty(annotation)){
                MDC.put(API_CODE.getKey(), annotation.value());
                log.info("-- {} --", annotation.value());
            }
        }
        filterChain.doFilter(request, response);
        MDC.remove(API_CODE.getKey());
    }
}
