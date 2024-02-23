package io.github.qyvlik.signv4.domain.model;

import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import io.github.qyvlik.signv4.domain.signer.Signatory;
import io.github.qyvlik.signv4.domain.signer.Signing;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

/**
 * @param requestDateTime UTC 时间，格式： yyyyMMdd'T'HHmmss'Z'
 *                        在凭证范围内使用的日期和时间。该值是采用 ISO 8601 格式的当前 UTC 时间（例如 20130524T000000Z）。
 * @param authorization
 * @param request
 */
public record RequestData(String requestDateTime,
                          Authorization authorization,
                          CanonicalRequest request) {
    /**
     * 在服务端构建
     *
     * @param method  请求方法
     * @param uri     请求路径，路径中的 `%2F` 必须转义回 `/`
     * @param headers 请求头
     * @param query   查询字符串，query 的 value 必须经过 {@UriEncoder.rfc3986Encode} 编码
     * @param body    原始请求体
     */
    public static RequestData create(String method,
                                     String uri,
                                     SortedMap<String, String> headers,
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
                requestDateTime,
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
    public static String getSortedHeadersString(SortedMap<String, String> headers, SortedSet<String> signedHeaders) {
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
}
