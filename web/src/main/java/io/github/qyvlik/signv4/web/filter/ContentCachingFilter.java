package io.github.qyvlik.signv4.web.filter;

//import org.springframework.web.filter.OncePerRequestFilter;

import io.github.qyvlik.signv4.web.request.CachedRequest;
import io.github.qyvlik.signv4.web.request.CachedRequestFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.converter.support.AllEncompassingFormHttpMessageConverter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * https://www.baeldung.com/spring-reading-httpservletrequest-multiple-times
 */
public class ContentCachingFilter extends OncePerRequestFilter {
    private final CachedRequestFactory factory;

    public ContentCachingFilter() {
        this(false);
    }

    public ContentCachingFilter(boolean mock) {
        this.factory = new CachedRequestFactory(
                new AllEncompassingFormHttpMessageConverter(),
                mock
        );
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        if (httpServletRequest instanceof CachedRequest) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }
        CachedRequest cachedRequest = this.factory.caching(httpServletRequest);
        filterChain.doFilter(cachedRequest, httpServletResponse);
    }
}