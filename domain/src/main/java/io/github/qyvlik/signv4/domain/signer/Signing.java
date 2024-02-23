package io.github.qyvlik.signv4.domain.signer;

import com.google.common.hash.Hashing;
import com.google.common.io.BaseEncoding;
import io.github.qyvlik.signv4.domain.model.Calculating;
import io.github.qyvlik.signv4.domain.model.CanonicalRequest;
import io.github.qyvlik.signv4.domain.model.Credential;
import io.github.qyvlik.signv4.domain.model.StringToSign;

import java.nio.charset.StandardCharsets;

/**
 * 签名过程
 * https://docs.aws.amazon.com/zh_cn/IAM/latest/UserGuide/create-signed-request.html#create-signed-request-steps
 *
 * @param request      plainText = request.plainText()
 * @param credential
 * @param stringToSign stringToSign = algorithm + '\n' + requestDateTime + '\n' + credentialScope + '\n' + hashedCanonicalRequest
 *                     credentialScope = date + '\' + region + '\' + service + '\' + 'aws4_request'
 *                     hashedCanonicalRequest = SHA256(plainText)
 * @param signature
 */
public record Signing(CanonicalRequest request,
                      Credential credential,
                      StringToSign stringToSign,
                      Calculating calculating,
                      String signature) {

    /**
     * @param request         规范请求
     * @param credential      凭证
     * @param algorithm       用于创建规范请求的哈希的算法。对于 SHA-256，算法是 AWS4-HMAC-SHA256。
     * @param requestDateTime 该值是采用 ISO 8601 格式的当前 UTC 时间（例如 20130524T000000Z）
     * @param signatory       签名人
     * @return 签名信息
     */
    public static Signing signing(CanonicalRequest request,
                                  Credential credential,
                                  String algorithm,
                                  String requestDateTime,
                                  Signatory signatory) {
        StringToSign strToSign = StringToSign.create(request, credential, algorithm, requestDateTime);
        String stringToSign = strToSign.stringToSing();

        final String date = credential.date();
        final String region = credential.region();
        final String service = credential.service();

        byte[] kDate = signatory.signature(date.getBytes(StandardCharsets.UTF_8));
        byte[] kRegion = Hashing.hmacSha256(kDate)
                .hashString(region, StandardCharsets.UTF_8).asBytes();
        byte[] kService = Hashing.hmacSha256(kRegion)
                .hashString(service, StandardCharsets.UTF_8).asBytes();
        byte[] kSigning = Hashing.hmacSha256(kService)
                .hashString("aws4_request", StandardCharsets.UTF_8).asBytes();
        String signature = Hashing.hmacSha256(kSigning)
                .hashString(stringToSign, StandardCharsets.UTF_8).toString();

        Calculating calculating = new Calculating(
                BaseEncoding.base16().lowerCase().encode(kDate),
                BaseEncoding.base16().lowerCase().encode(kRegion),
                BaseEncoding.base16().lowerCase().encode(kService),
                BaseEncoding.base16().lowerCase().encode(kSigning)
        );

        return new Signing(request, credential, strToSign, calculating, signature);
    }


    /**
     * 返回 Authorization 的值
     */
    public String authorization() {
        return this.stringToSign.algorithm() + " "
                + "Credential=" + credential.accessKey() + "/" + credential.getCredentialScope() + ","
                + "SignedHeaders=" + request.signedHeaders() + ","
                + "Signature=" + signature;
    }


}
