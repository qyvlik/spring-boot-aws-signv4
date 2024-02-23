package io.github.qyvlik.signv4.domain.utils;

import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.TreeMap;

public class QueryStringUtils {

    public static TreeMap<String, String> sortQueryString(String queryString) {
        TreeMap<String, String> queryMap = new TreeMap<>();
        if (StringUtils.isBlank(queryString)) {
            return queryMap;
        }
        for (String kv : queryString.split("&")) {
            final String[] pair = kv.split("=");
            final String key = pair[0];
            if (pair.length == 1) {
                queryMap.put(key, "");
                continue;
            }
            final String encodeValue = pair[1];
            String fullDecodeValue = URLDecoder.decode(encodeValue, StandardCharsets.UTF_8);
            String value = UriEncoder.rfc3986Encode(fullDecodeValue, StandardCharsets.UTF_8);

            queryMap.put(key, value);
        }
        return queryMap;
    }
}
