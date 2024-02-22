package io.github.qyvlik.signv4.domain.model;

import org.apache.commons.lang3.StringUtils;

/**
 * 凭证
 * https://docs.aws.amazon.com/zh_cn/IAM/latest/UserGuide/signing-elements.html#authentication
 * 日期： 您可以使用 date 标头或 x-amz-date 标头，或将 x-amz-date 作为查询参数包含在内。
 * 如果无法找到 x-amz-date 标头，则需要查找 date 标头。
 *
 * @param accessKey   访问密钥 ID
 *                    示例数据： AKIAIOSFODNN7EXAMPLE
 * @param date        YYYYMMDD 格式的日期
 *                    示例数据： 20220830
 * @param region      区域代码: 必须使用小写字符
 *                    示例数据： us-east-1
 * @param service     服务代码： 必须使用小写字符
 *                    示例数据： ec2
 * @param aws4Request 终止字符串
 *                    固定数据： aws4_request
 */
public record Credential(
        String accessKey,
        String date,
        String region,
        String service,
        String aws4Request) {
    public static Credential parse(String credential) {
        if (StringUtils.isBlank(credential)) {
            throw new IllegalArgumentException("credential is blank");
        }
        String[] credentialArray = credential.split("/");
        if (credentialArray.length != 5) {
            throw new IllegalArgumentException("credential must contain 5 /");
        }
        return new Credential(
                credentialArray[0],
                credentialArray[1],
                credentialArray[2],
                credentialArray[3],
                credentialArray[4]
        );
    }

    /**
     * 凭证范围
     * 这会将生成的签名限制在指定的区域和服务范围内。该字符串采用以下格式：YYYYMMDD/region/service/aws4_request。
     */
    public String getCredentialScope() {
        return this.date + "/"
                + this.region + "/"
                + this.service + "/"
                + this.aws4Request;
    }
}
