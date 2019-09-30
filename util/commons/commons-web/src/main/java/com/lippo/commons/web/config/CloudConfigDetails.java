package com.lippo.commons.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

//@RefreshScope
@Configuration
public class CloudConfigDetails {

//    @Value("${authentication.server.url}")
    private String authenticationServerURL;

//    @Value("${token.cache.duration.in.minutes}")
    private int tokenCacheDurationInMinutes;

    public String getAuthenticationServerURL() {
        return authenticationServerURL;
    }

    public int getTokenCacheDurationInMinutes() {
        return tokenCacheDurationInMinutes;
    }

    /**
     * Generates a default HTTP Client which is mostly used only for authentication purposes
     *
     * @return
     */
    @ConditionalOnMissingBean
    @Bean(name = "customRestTemplate")
    public RestTemplate restTemplate() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(5000);
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return new RestTemplate(factory);
    }


    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
        filter.setIncludeQueryString(true);
        filter.setIncludePayload(true);
        filter.setMaxPayloadLength(1000);
        filter.setIncludeHeaders(true);
        filter.setAfterMessagePrefix("REQUEST DATA : ");
        return filter;
    }
}
