package com.lippo.commons.web;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.lippo.commons.web.config.CloudConfigDetails;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class LippoAuthenticateService {

    private static final Logger logger = LogManager.getLogger(LippoAuthenticateService.class);

    private final Cache<String, User> tokenCache;
    private final RestTemplate restTemplate;
    private CloudConfigDetails cloudConfigDetails;

    public LippoAuthenticateService(RestTemplate restTemplate, CloudConfigDetails cloudConfigDetails) {
        this.restTemplate = restTemplate;
        this.cloudConfigDetails = cloudConfigDetails;
        tokenCache = CacheBuilder.newBuilder()
                .expireAfterAccess(this.cloudConfigDetails.getTokenCacheDurationInMinutes(), TimeUnit.MINUTES)
                .build();
    }

    public Optional<UsernamePasswordAuthenticationToken> retrieveAuthentication(String token) {

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

    public Optional<User> retrieveUser(String token) {
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
}
