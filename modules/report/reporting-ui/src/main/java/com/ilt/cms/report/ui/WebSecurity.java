package com.ilt.cms.report.ui;

import com.lippo.commons.web.config.CloudConfigDetails;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.web.client.RestTemplate;

@Configuration
//@EnableWebSecurity
@EntityScan("com.lippo.cms")
@EnableMongoRepositories(value = "com.lippo.cms.repository")
//@ImportAutoConfiguration(RefreshAutoConfiguration.class)
//@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
//public class WebSecurity extends WebSecurityConfigurerAdapter {
public class WebSecurity {

    private RestTemplate restTemplate;
    private CloudConfigDetails cloudConfigDetails;

    public WebSecurity(CloudConfigDetails cloudConfigDetails, RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.cloudConfigDetails = cloudConfigDetails;
    }


//    protected void configure(HttpSecurity http) throws Exception {
////        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
//
////        http.authorizeRequests()
////                .antMatchers("/test/**").anonymous()
////                .antMatchers("/images/**").anonymous()
////                .antMatchers("/css/**").anonymous()
////                .antMatchers("/js/**").anonymous()
////                .antMatchers("/favicon.ico").anonymous()
////                .antMatchers("/frameset?**").anonymous()
////                .antMatchers("/frameset**").anonymous()
////                .antMatchers("/frameset").anonymous()
////                .antMatchers("/frameset*").anonymous()
////                .anyRequest().authenticated()
////                .and()
////                // yes this can be annotated but this is kept here so that someone new can easily navigate it
////                .addFilter(new CustomAuthorizationFilter(authenticationManagerBean(), cloudConfigDetails, restTemplate));
//
//        http.csrf().disable().authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                // yes this can be annotated but this is kept here so that someone new can easily navigate it
////                .addFilter(new AuthorizationFilter());
//        ;
//    }
//
//
////    @Configuration
////    static class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {
////        @Bean(name = "authenticationManager")
////        public AuthenticationManager authenticationManagerBean() throws  Exception {
////            return super.authenticationManagerBean();
////        }
////    }
//
////    @Configuration
////    static class WebConfiguration {
////
////
////        @Bean
////        public WebMvcConfigurer corsConfigurer() {
////            return new WebMvcConfigurerAdapter() {
////                @Override
////                public void addCorsMappings(CorsRegistry registry) {
////                    registry.addMapping("/**").allowedOrigins("*");
////                }
////            };
////        }
////    }
////
////    @Bean
////    public AuditorAware<String> auditor() {
////        return () -> ((String) SecurityContextHolder.getContext()
////                .getAuthentication().getPrincipal());
////    }
}
