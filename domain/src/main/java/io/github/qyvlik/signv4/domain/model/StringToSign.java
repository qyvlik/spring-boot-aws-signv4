package io.github.qyvlik.signv4.domain.model;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

/**
 * https://docs.aws.amazon.com/zh_cn/IAM/latest/UserGuide/create-signed-request.html#create-string-to-sign
 * <p>
 * algorithm + '\n' + requestDateTime + '\n' + credentialScope + '\n' + hashedCanonicalRequest
 *
 * @param algorithm
 * @param requestDateTime
 * @param credentialScope
 * @param hashedCanonicalRequest
 */
public record StringToSign(String algorithm,
                           String requestDateTime,
                           String credentialScope,
                           String hashedCanonicalRequest,
                           String canonicalRequest) {

    public static StringToSign create(CanonicalRequest request,
                                      Credential credential,
                                      String algorithm,
                                      String requestDateTime) {
        String plainText = request.toPlainText();
        String hashedCanonicalRequest = Hashing.sha256().hashString(plainText, StandardCharsets.UTF_8).toString();
        String credentialScope = credential.getCredentialScope();
        return new StringToSign(
                algorithm,
                requestDateTime,
                credentialScope,
                hashedCanonicalRequest,
                plainText
        );
    }


    public String stringToSing() {
        return algorithm + '\n'
                + requestDateTime + '\n'
                + credentialScope + '\n'
                + hashedCanonicalRequest;
    }
}
