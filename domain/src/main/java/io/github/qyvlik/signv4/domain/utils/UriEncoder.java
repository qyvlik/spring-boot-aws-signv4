package io.github.qyvlik.signv4.domain.utils;

import java.net.URLEncoder;
import java.nio.charset.Charset;

public class UriEncoder {
    /**
     * https://github.com/aws/aws-sdk-java/blob/master/aws-java-sdk-core/src/main/java/com/amazonaws/util/SdkHttpUtils.java#L66
     * @param s
     * @param charset
     * @return
     */
    public static String rfc3986Encode(String s, Charset charset) {
        return URLEncoder.encode(s, charset)
                .replace("+", "%20")
                .replace("*", "%2A")
                .replace("%7E", "~");
    }
}
