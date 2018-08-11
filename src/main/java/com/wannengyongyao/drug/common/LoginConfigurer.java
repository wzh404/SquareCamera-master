package com.wannengyongyao.drug.common;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class LoginConfigurer implements WebMvcConfigurer {
    @Bean
    public LoginInterceptor logInterceptor() {
        return new LoginInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .allowedHeaders("*")
                .exposedHeaders("access_token")
                .allowCredentials(true).maxAge(3600);
    }

    @Bean("smsCache")
    public Cache<String, String> smsCache(){
        return CacheBuilder.newBuilder()
                .initialCapacity(1000)
                .expireAfterWrite(5, TimeUnit.MINUTES)
                .build();
    }
}
