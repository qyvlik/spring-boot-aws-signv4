package io.github.qyvlik.signv4.server.config;

import com.google.common.collect.ImmutableMap;
import io.github.qyvlik.signv4.domain.auth.Authenticating;
import io.github.qyvlik.signv4.domain.auth.SignatoryProvider;
import io.github.qyvlik.signv4.server.provider.SignatoryProviderService;
import io.github.qyvlik.signv4.web.filter.ContentCachingFilter;
import io.github.qyvlik.signv4.web.interceptor.AwsSignV4AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.time.Duration;
import java.util.Map;

@Configuration
public class BeansConfig {
    @Bean
    public FilterRegistrationBean<ContentCachingFilter> contentCachingFilter() {
        FilterRegistrationBean<ContentCachingFilter> registration = new FilterRegistrationBean<>();

        registration.setName("contentCachingFilter");
        registration.setFilter(new ContentCachingFilter());
        registration.addUrlPatterns("/api/v1/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);          // first filter

        return registration;
    }

    @Bean
    public SignatoryProviderService signatoryProviderService() {
        return new SignatoryProviderService(ImmutableMap.of("test1", "test1"));
    }

    @Bean
    public Authenticating authenticating(
            @Autowired SignatoryProviderService signatoryProviderService) {
        return new Authenticating(signatoryProviderService, Duration.ofSeconds(30));
    }

    @Bean
    public AwsSignV4AuthInterceptor awsSignV4Interceptor(
            @Autowired Authenticating authenticating) {
        return new AwsSignV4AuthInterceptor(authenticating);
    }
}
