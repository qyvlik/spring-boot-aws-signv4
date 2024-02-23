package io.github.qyvlik.signv4.web.interceptor;

import com.google.common.collect.Maps;
import io.github.qyvlik.signv4.domain.model.RequestData;
import io.github.qyvlik.signv4.domain.utils.QueryStringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.TreeMap;

public class RequestDataUtils {

    public static RequestData create(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        TreeMap<String, String> headers = getSortedHeadersFromRequest(request);
        TreeMap<String, String> query = QueryStringUtils.sortQueryString(request.getQueryString());
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        return RequestData.create(
                method,
                uri,
                headers,
                query,
                body
        );
    }


    /**
     * 请求标头都是小写
     */
    public static TreeMap<String, String> getSortedHeadersFromRequest(HttpServletRequest request) {
        TreeMap<String, String> headerMap = Maps.newTreeMap();
        Enumeration<String> enumeration = request.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            String name = enumeration.nextElement();
            String value = request.getHeader(name);
            headerMap.put(name.toLowerCase(), StringUtils.isBlank(value) ? "" : value.trim());
        }
        return headerMap;
    }

}
