package com.lippo.cms.inventory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lippo.commons.web.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestHelper {

    private final static ObjectMapper JSON_MAPPER;

    public static final String TOKEN = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbXSwiZXhwIjoxNTEyMjAyNzM3fQ.SEhI9pO0idsC2ozxyT9elJhZAskVagnYPkpBVHNN9nAtMKHqDbHPpnICM1BC6CC5975-yVLTCUr1VDvAoaHMmw";

    static {
        Jackson2ObjectMapperBuilder json = Jackson2ObjectMapperBuilder.json();
        json.indentOutput(true);
        json.serializationInclusion(JsonInclude.Include.NON_NULL);
        JSON_MAPPER = json.build();
    }

    public static MockHttpServletRequestBuilder httpRequestBuilder(MockHttpServletRequestBuilder requestBuilder, Object obj) throws JsonProcessingException {
        if (obj != null) {
            return requestBuilder.accept(MediaType.APPLICATION_JSON).content(JSON_MAPPER.writeValueAsBytes(obj))
                    .contentType(MediaType.APPLICATION_JSON);
        } else {
            return requestBuilder.accept(MediaType.APPLICATION_JSON).content("{}")
                    .contentType(MediaType.APPLICATION_JSON);
        }
    }

    public static void mockAuthenticationResponse(RestTemplate restTemplate, User user) {
        ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(responseEntity.getStatusCode()).thenReturn(HttpStatus.OK);
        when(responseEntity.getBody()).thenReturn(user);
        when(restTemplate.exchange(any(RequestEntity.class), eq(User.class))).thenReturn(responseEntity);
    }

    public static User userDetails(List<String> roles) {
        User user = new User();
        user.setMessage("Success");
        User.Payload payload = new User().newPayload();
        payload.setRoles(roles);
        payload.setUserName("username");
        user.setPayload(payload);
        return user;
    }

    public static User userDetails() {
        return userDetails(Arrays.asList("ROLE_PATIENT_ACCESS", "ROLE_PATIENT_REGISTRATION"));
    }
    public static ObjectMapper getJsonMapper() {
        return JSON_MAPPER;
    }
}
