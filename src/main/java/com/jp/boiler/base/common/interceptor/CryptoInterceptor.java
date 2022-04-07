package com.jp.boiler.base.common.interceptor;

import com.jp.boiler.base.common.annotation.CryptoData;
import com.jp.boiler.base.common.context.CryptoDataContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class CryptoInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean shouldCrypto = false;
        if(handler instanceof HandlerMethod){
            final HandlerMethod method = (HandlerMethod) handler;
            final CryptoData annotation = method.getMethod().getAnnotation(CryptoData.class);
            if (annotation != null) shouldCrypto = true;
        }

        CryptoDataContext.setCryptoDataContext(shouldCrypto);
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
