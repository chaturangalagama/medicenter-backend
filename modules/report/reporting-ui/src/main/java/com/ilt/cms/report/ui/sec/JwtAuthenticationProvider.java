package com.ilt.cms.report.ui.sec;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lippo.commons.web.User;
import com.lippo.commons.web.config.CloudConfigDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LogManager.getLogger(JwtAuthenticationProvider.class);
    private Cache<String, User> tokenCache;
    private final RestTemplate restTemplate;
    @Autowired
    private CloudConfigDetails cloudConfigDetails;

    public JwtAuthenticationProvider() {
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    public void init() {
        tokenCache = CacheBuilder.newBuilder()
                .expireAfterAccess(cloudConfigDetails.getTokenCacheDurationInMinutes(), TimeUnit.MINUTES)
                .build();
    }

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        JwtAuthenticationToken token = (JwtAuthenticationToken) auth;

        Optional<UsernamePasswordAuthenticationToken> authenticationOpt = retrieveAuthentication(token.getToken());
        if (authenticationOpt.isPresent()) {
            UsernamePasswordAuthenticationToken authentication = authenticationOpt.get();
            logger.info("User information : username[{}] roles[{}]", authentication.getPrincipal(),
                    authentication.getAuthorities());
            return authenticationOpt.get();
        } else {
            logger.error("No authentication response");
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Optional<UsernamePasswordAuthenticationToken> retrieveAuthentication(String token) {

        Optional<User> userOpt = retrieveUser(token);
        if (!userOpt.isPresent()) {
            return Optional.empty();
        }
        User user = userOpt.get();
        return Optional.of(new UsernamePasswordAuthenticationToken(user.getPayload().getUserName(),
                null, user.getPayload()
                .getRoles().stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
        ));

    }

    private Optional<User> retrieveUser(String token) {
        User user = tokenCache.getIfPresent(token);
        if (user == null) {
            ResponseEntity<User> responseEntity = httpRequest(token);
            if (!responseEntity.getStatusCode().is2xxSuccessful()) {
                logger.error("Authentication server returned none success code ["
                        + responseEntity.getStatusCode() + "] response [" + responseEntity.getBody() + "]");
                return Optional.empty();
            } else {
                user = responseEntity.getBody();
                tokenCache.put(token, user);
            }
        }
        return Optional.of(user);
    }

    private ResponseEntity<User> httpRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, token);

        ResponseEntity<User> responseEntity;
        try {
            logger.debug("Getting use entity from [{}]", cloudConfigDetails.getAuthenticationServerURL());
            responseEntity = restTemplate.exchange(new RequestEntity<User>(headers, HttpMethod.POST,
                    new URI(cloudConfigDetails.getAuthenticationServerURL())), User.class);
        } catch (Exception e) {
            logger.error("Error authenticating the token : [" + token + "]", e);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        return responseEntity;
    }

    public void setCloudConfigDetails(CloudConfigDetails cloudConfigDetails) {
        this.cloudConfigDetails = cloudConfigDetails;
    }
}
