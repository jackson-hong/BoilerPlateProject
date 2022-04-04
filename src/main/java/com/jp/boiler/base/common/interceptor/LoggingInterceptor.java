package com.jp.boiler.base.common.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class LoggingInterceptor implements HandlerInterceptor {

    private final ObjectMapper objectMapper;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        if(request instanceof ContentCachingRequestWrapper){
            try {
                final ContentCachingRequestWrapper cachingRequest = (ContentCachingRequestWrapper) request;
                log.info(">REQ BEFORE CONTROLLER : {}", objectMapper.readTree(cachingRequest.getContentAsByteArray()));
            }catch (Exception e){
                log.warn(">REQ Logging filter exception : {}", e.getLocalizedMessage());
            }
        }

        if(response instanceof ContentCachingResponseWrapper){
            String responseContentType = Optional.ofNullable(response.getContentType()).orElse("");

            try{
                final ContentCachingResponseWrapper cachingResponse = (ContentCachingResponseWrapper) response;

                if(responseContentType.contains("application/json")){
                    log.info(">RES BEFORE CONTROLLER : {}", objectMapper.readTree(cachingResponse.getContentAsByteArray()));
                }else {
                    log.info(">RES : {} <...omit...>", Arrays.toString(cachingResponse.getContentAsByteArray()).substring(0,256));
                }
            }catch (Exception e){
                log.warn(">RES Logging filter exception : {}", e.getLocalizedMessage());
            }
        }
    }
}
