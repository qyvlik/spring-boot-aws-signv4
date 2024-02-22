package io.github.qyvlik.signv4.domain.model;

import com.google.common.collect.ImmutableSortedMap;
import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import io.github.qyvlik.signv4.domain.signer.Signatory;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public record RequestData(String method,
                          String uri,
                          String requestDateTime,
                          ImmutableSortedMap<String, String> headers,
                          ImmutableSortedMap<String, String> query,
                          Authorization authorization,
                          CanonicalRequest request) {

    /**
     * 在服务端构建
     *
     * @param method  请求方法
     * @param uri     请求路径
     * @param headers 请求头
     * @param query   查询字符串
     * @param body    请求体
     */
    public static RequestData create(String method,
                                     String uri,
                                     Map<String, String> headers,
                                     SortedMap<String, String> query,
                                     byte[] body) {
        Authorization authorization = null;
        if (headers.containsKey("authorization")) {
            authorization = Authorization.parse(headers.get("authorization"));
        } else {
            authorization = Authorization.parse(query);
        }

        CanonicalRequest canonicalRequest = new CanonicalRequest(
                method,
                uri,
                getSortedQueryString(query),
                getSortedHeadersString(headers, authorization.signedHeaders()),
                authorization.toSignedHeadersString(),
                Hashing.sha256().hashBytes(body).toString()
        );

        // must YYYYMMDDTHHMMSSZ
        String requestDateTime = getValueFromMap(headers, "x-amz-date", "date");
        if (StringUtils.isBlank(requestDateTime)) {
            requestDateTime = query.get("X-Amz-Date");
        }

        return new RequestData(
                method,
                uri,
                requestDateTime,
                ImmutableSortedMap.copyOf(headers),
                ImmutableSortedMap.copyOf(query),
                authorization,
                canonicalRequest
        );
    }

    /**
     * @param signatory 远程或者本地 HMAC-SHA256 签名
     * @return 签名
     */
    public Signing signature(Signatory signatory) {
        return Signing.signing(request,
                authorization.credential(),
                authorization.algorithm(),
                requestDateTime,
                signatory);
    }

    /**
     *
     */
    public static String getSortedHeadersString(Map<String, String> headers, SortedSet<String> signedHeaders) {
        List<String> headerList = Lists.newArrayList();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            if (signedHeaders.contains(entry.getKey())) {
                headerList.add(entry.getKey() + ":" + entry.getValue());
            }
        }
        return String.join("\n", headerList) + "\n";
    }

    /**
     * 不包含 X-Amz-Signature
     */
    public static String getSortedQueryString(SortedMap<String, String> query) {
        List<String> queryList = Lists.newArrayList();
        for (Map.Entry<String, String> entry : query.entrySet()) {
            if (entry.getKey().equals("X-Amz-Signature")) {
                continue;
            }
            queryList.add(entry.getKey() + "=" + entry.getValue());
        }
        return String.join("&", queryList);
    }

    /**
     * @param headerName          优先获取的key
     * @param alternateHeaderName 替补 key
     */
    public static String getValueFromMap(Map<String, String> map, String headerName, String alternateHeaderName) {
        String value = map.get(headerName);
        if (StringUtils.isBlank(value)) {
            return map.get(alternateHeaderName);
        }
        return value;
    }



    public static Long parseRequestDateTime(String requestDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(requestDateTime).getTime();
        } catch (ParseException e) {
            return null;
        }
    }
}
