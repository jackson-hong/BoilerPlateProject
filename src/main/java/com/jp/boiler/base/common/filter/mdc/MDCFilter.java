package com.jp.boiler.base.common.filter.mdc;

import com.jp.boiler.base.common.type.MDCKey;
import lombok.extern.slf4j.Slf4j;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
public class MDCFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final String trxId = UUID.randomUUID().toString().substring(0,8);
        MDC.put(MDCKey.TRX_ID.getKey(), trxId);

        filterChain.doFilter(servletRequest, servletResponse);

        MDC.remove(MDCKey.TRX_ID.getKey());
    }
}
