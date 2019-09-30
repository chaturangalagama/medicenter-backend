package com.lippo.commons.web;

import com.lippo.commons.web.config.CloudConfigDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.client.RestTemplate;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LogManager.getLogger(AuthorizationFilter.class);
    private static final String TOKEN_HEADER = "Authorization";

    private LippoAuthenticateService authenticateService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, CloudConfigDetails cloudConfigDetails,
                               RestTemplate restTemplate) {
        super(authenticationManager);

        authenticateService = new LippoAuthenticateService(restTemplate, cloudConfigDetails);

    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {

        if (!req.getMethod().equals(HttpMethod.POST.name())
                && !req.getMethod().equals(HttpMethod.GET.name())) {
            chain.doFilter(req, res);
        } else {
            String header = req.getHeader(TOKEN_HEADER);
            if (header == null) {
                throw new ServletException("Token header is missing");
            }
            long start = System.currentTimeMillis();
            Optional<UsernamePasswordAuthenticationToken> authenticationOpt = authenticateService.retrieveAuthentication(header);
            if (authenticationOpt.isPresent()) {
                UsernamePasswordAuthenticationToken authentication = authenticationOpt.get();
                logger.trace("User information : username[{}] roles[{}]", authentication.getPrincipal(),
                        authentication.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                logger.error("No authentication response");
            }
            logger.debug("User Role Authentication process duration [{}]ms", (System.currentTimeMillis() - start));
            chain.doFilter(req, res);
        }
    }
}
