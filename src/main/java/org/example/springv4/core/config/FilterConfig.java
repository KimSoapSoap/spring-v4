package org.example.springv4.core.config;

import jakarta.servlet.FilterRegistration;
import org.example.springv4.core.filter.JwtAuthorizationFilter;
import org.example.springv4.user.User;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * IoC가 무엇인가? 제어의 역전이다. 개발자가 new 하는 것이 아니라 Spring이 new를 해서 스프링이 객체를 생성해서 관리해준다.
 *
 * @Controller, @RestController, @Service, @Repository, @Component, @Configuration
 */


@Configuration
public class FilterConfig {


    @Bean
    public FilterRegistrationBean<?> jwtAuthorizationFilter() {
        FilterRegistrationBean<JwtAuthorizationFilter> bean
                = new FilterRegistrationBean<JwtAuthorizationFilter>(new JwtAuthorizationFilter());
        bean.addUrlPatterns("/api/*"); // *는 모든 경로.  *표 1개 올 때가 있고 2개가 올 때가 있다. 1개 해보고 안 되면 2개 넣어준다.
        return bean;
    }
}
