package com.lippo.commons.web;

import com.lippo.commons.util.CommonUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TransactionFilter implements Filter {

    private final Logger logger = LogManager.getLogger(TransactionFilter.class);

    @Value("${system.transaction.id.prefix:0}")
    private int transactionSystemId;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //not need to do anything
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        long start = System.currentTimeMillis();
        try {
            // Setup MDC data
            MDC.put("transaction-id", transactionSystemId + CommonUtils.transactionId());
            chain.doFilter(request, response);
        } finally {
            // Tear down MDC data
            logger.info("Request process duration [{}]ms", (System.currentTimeMillis() - start));
            MDC.remove("transaction-id");
        }
    }

    @Override
    public void destroy() {
        //not need to do anything
    }
}
