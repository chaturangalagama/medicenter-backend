package com.ilt.cms.pm.rest.configuration;

import com.lippo.commons.web.AuthorizationFilter;
import com.lippo.commons.web.config.CloudConfigDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpMethod;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Optional;

//@EnableWebSecurity
//@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
public class WebSecurity {//extends WebSecurityConfigurerAdapter {

//    private RestTemplate restTemplate;
//    private CloudConfigDetails cloudConfigDetails;
//
//    public WebSecurity(CloudConfigDetails cloudConfigDetails, RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//        this.cloudConfigDetails = cloudConfigDetails;
//    }
//
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        //http.httpBasic();
//
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
//        http.csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
////                 yes this can be annotated but this is kept here so that someone new can easily navigate it
//                .addFilter(new AuthorizationFilter(authenticationManager(), cloudConfigDetails, restTemplate));
//    }
//
//    @Configuration
//    static class WebConfiguration {
//        @Bean
//        public WebMvcConfigurer corsConfigurer() {
//            return new WebMvcConfigurerAdapter() {
//                @Override
//                public void addCorsMappings(CorsRegistry registry) {
//                    registry.addMapping("/**").allowedOrigins("*");
//                }
//            };
//        }
//    }
//
//    @Bean
//    public AuditorAware<String> auditor() {
//        return () -> Optional.of(((String) SecurityContextHolder.getContext()
//                .getAuthentication().getPrincipal()));
//    }
}
