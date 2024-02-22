package io.github.qyvlik.signv4.domain.model;

import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeSet;


/**
 * https://docs.aws.amazon.com/zh_cn/IAM/latest/UserGuide/create-signed-request.html#add-signature-to-request
 * <p>
 * example : Authorization: AWS4-HMAC-SHA256 Credential=AKIAIOSFODNN7EXAMPLE/20220830/us-east-1/ec2/aws4_request,SignedHeaders=host;x-amz-date,Signature=calculated-signature
 *
 * @param algorithm
 * @param credential
 * @param signedHeaders
 * @param signature
 */
public record Authorization(String algorithm,
                            Credential credential,
                            ImmutableSortedSet<String> signedHeaders,
                            String signature) {
    /**
     * 从请求标头构造
     */
    public static Authorization parse(String authorization) {
        if (StringUtils.isBlank(authorization)) {
            throw new IllegalArgumentException("authorization is blank");
        }
        // Algorithm Credential=xxx, SignedHeaders=xx;xx, Signature=xxx
        String[] array = authorization.split(",");
        if (array.length != 3) {
            throw new IllegalArgumentException("authorization invalidate");
        }

        String[] algorithmAndCredential = array[0].split(" ");
        if (algorithmAndCredential.length != 2) {
            throw new IllegalArgumentException("authorization invalidate");
        }
        String algorithm = algorithmAndCredential[0];
        String credentialString = getValueFromKeyEqValue(algorithmAndCredential[1], "Credential");
        String signedHeadersString = getValueFromKeyEqValue(array[1].trim(), "SignedHeaders");
        String signatureString = getValueFromKeyEqValue(array[2].trim(), "Signature");
        return new Authorization(algorithm,
                Credential.parse(credentialString),
                ImmutableSortedSet.copyOf(parseSignedHeaders(signedHeadersString)),
                signatureString
        );
    }

    /**
     * 从查询字符串构造
     */
    public static Authorization parse(Map<String, String> query) {
        String credential = URLDecoder.decode(query.get("X-Amz-Credential"), StandardCharsets.UTF_8);
        String signedHeaders = URLDecoder.decode(query.get("X-Amz-SignedHeaders"), StandardCharsets.UTF_8);
        return new Authorization(
                query.get("X-Amz-Algorithm"),
                Credential.parse(credential),
                ImmutableSortedSet.copyOf(parseSignedHeaders(signedHeaders)),
                query.get("X-Amz-Signature")
        );
    }

    public static String getValueFromKeyEqValue(String keyEqValue, String key) {
        if (!keyEqValue.startsWith(key + "=")) {
            throw new IllegalArgumentException(keyEqValue + " not start withs " + key + "=");
        }
        String[] kv = keyEqValue.split("=");
        return kv[1];
    }

    public static TreeSet<String> parseSignedHeaders(String signedHeaders) {
        if (StringUtils.isBlank(signedHeaders)) {
            throw new IllegalArgumentException("signedHeaders is blank");
        }
        String[] headers = signedHeaders.split(";");
        return Sets.newTreeSet(Arrays.asList(headers));
    }

    public String toSignedHeadersString() {
        return String.join(";", this.signedHeaders);
    }
}
