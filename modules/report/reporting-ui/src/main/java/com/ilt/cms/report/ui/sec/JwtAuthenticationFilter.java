package com.ilt.cms.report.ui.sec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final Logger logger = LogManager.getLogger(com.lippo.commons.web.AuthorizationFilter.class);
    private static final String TOKEN_HEADER = "Authorization";


    public JwtAuthenticationFilter() {
        super("/**");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return true;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String header = request.getHeader(TOKEN_HEADER);
        if (header == null) {
            header = request.getParameter(TOKEN_HEADER);
        }

        if (header == null) {
            Authentication authenticate = (Authentication) request.getSession().getAttribute("auth-token");
            if (authenticate != null) {
                return authenticate;
            }
        }
//        if (header != null) {
//            request.getSession().setAttribute("header-token", header);
//        } else {
//            header = (String) request.getSession().getAttribute("header-token");
//        }

        if (header == null) {
            return new AnonymousAuthenticationToken("anonymousUser", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        } else {
            return getAuthenticationManager().authenticate(new JwtAuthenticationToken(header));
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        super.successfulAuthentication(request, response, chain, authResult);

        // As this authentication is in HTTP header, after success we need to continue the request normally
        // and return the response as if the resource was not secured at all
        chain.doFilter(request, response);
    }
}
