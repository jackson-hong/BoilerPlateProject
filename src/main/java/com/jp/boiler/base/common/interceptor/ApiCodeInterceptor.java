package com.jp.boiler.base.common.interceptor;

import aj.org.objectweb.asm.Handle;
import com.jp.boiler.base.common.annotation.ApiCode;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.jp.boiler.base.common.type.MDCKey.API_CODE;

@Slf4j
@Component
public class ApiCodeInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if(handler instanceof HandlerMethod){
            final HandlerMethod method = (HandlerMethod) handler;
            final ApiCode annotation = method.getMethod().getAnnotation(ApiCode.class);
            if(annotation != null){
                log.info("-- {} --", annotation.value());
                MDC.put(API_CODE.getKey(), annotation.value());
            }
        }

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        MDC.remove(API_CODE.getKey());
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
