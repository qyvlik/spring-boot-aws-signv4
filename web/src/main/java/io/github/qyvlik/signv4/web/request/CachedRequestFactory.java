package io.github.qyvlik.signv4.web.request;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;


import java.io.IOException;
import java.io.InputStream;

public class CachedRequestFactory {
    public static final String PARSE_FORM_BODY_METHOD = "POST";

    private final FormHttpMessageConverter converter;
    private final boolean mock;                         // spring-mvc mock

    public CachedRequestFactory(FormHttpMessageConverter converter, boolean mock) {
        this.converter = converter;
        this.mock = mock;
    }

    public CachedRequest caching(HttpServletRequest request) throws IOException {
        if (request instanceof CachedRequest) {
            return (CachedRequest) request;
        }

        InputStream requestInputStream = request.getInputStream();
        byte[] cachedBody = StreamUtils.copyToByteArray(requestInputStream);
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>(0);

        if (shouldParse(request)) {
            HttpInputMessage inputMessage = new ServletServerHttpRequest(request) {
                @Override
                public InputStream getBody() throws IOException {
                    return new CachedInputStream(cachedBody);
                }
            };
            form = converter.read(null, inputMessage);
        }

        return new CachedRequest(
                request,
                cachedBody,
                form
        );
    }

    public boolean shouldParse(HttpServletRequest request) {
        if (mock) {
            return false;
        }
        if (!PARSE_FORM_BODY_METHOD.equals(request.getMethod())) {
            return false;
        }
        try {
            MediaType mediaType = MediaType.parseMediaType(request.getContentType());
            return MediaType.APPLICATION_FORM_URLENCODED.includes(mediaType);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
