package io.github.qyvlik.signv4.server.config;

import io.github.qyvlik.signv4.web.interceptor.AwsSignV4AuthInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SignV4Config implements WebMvcConfigurer {

    @Resource
    private AwsSignV4AuthInterceptor awsSignV4AuthInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(awsSignV4AuthInterceptor)
                .addPathPatterns("/api/v1/**")
        ;
    }
}
