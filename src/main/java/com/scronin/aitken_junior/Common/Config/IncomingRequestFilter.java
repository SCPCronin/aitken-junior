package com.scronin.aitken_junior.Common.Config;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class IncomingRequestFilter implements Filter {
    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String MDC_KEY = "correlationId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String correlationId = getCorrelationIdFromHeader((HttpServletRequest) request);
            MDC.put(MDC_KEY, correlationId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_KEY);
        }
    }

    private String getCorrelationIdFromHeader(HttpServletRequest request) {
        String header = request.getHeader(CORRELATION_ID_HEADER);
        return (header != null && !header.isEmpty()) ? header : UUID.randomUUID().toString();
    }
}