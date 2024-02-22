package io.github.qyvlik.signv4.domain.model;

/**
 * 规范请求
 * https://docs.aws.amazon.com/zh_cn/IAM/latest/UserGuide/create-signed-request.html#create-canonical-request
 *
 * @param method        HTTPMethod
 * @param uri           CanonicalUri
 *                      绝对路径组件 URL 的 URI 编码版本（主机和以问号字符（？）开头的查询字符串参数之间的所有内容）。
 *                      如果绝对路径为空，则使用正斜杠字符（/）。
 * @param queryString   CanonicalQueryString
 *                      用连字符（&）分隔的 URL 编码的查询字符串参数。
 *                      对预留字符（包括空格字符）进行百分号编码。
 *                      分别对名称和值进行编码。如果参数为空，请在编码之前在参数名称后面加上等号。
 *                      编码后，按键名称的字母顺序对参数进行排序。
 *                      如果没有查询字符串，请使用空字符串 ("")。queryString 不用 ? 开头
 * @param headers       CanonicalHeaders
 *                      将签署的请求标头及其值，由换行符分隔。
 *                      标头名称必须使用小写字符，必须按字母顺序显示，必须在后面加上冒号（:）。
 *                      对于这些值，去除所有前导空格或尾部空格，将连续空格转换为单个空格，并使用逗号分隔多值标头的值。
 *                      您必须在签名中包含 host 标头（HTTP/1.1）或 :authority 标头（HTTP/2）以及任何 x-amz-* 标头。
 *                      您可选择在签名中包含其他标准标头，例如 content-type。
 * @param signedHeaders SignedHeaders
 *                      包含在 CanonicalHeaders 中的标头列表，由分号（;）分隔。
 *                      这表明哪些标头是签名过程的一部分。
 *                      标头名称必须使用小写字符，必须按字母顺序显示。
 * @param hashedPayload HashedPayload
 *                      使用 HTTP 请求正文中的负载作为哈希函数的输入创建的字符串。
 *                      此字符串使用小写十六进制字符。
 *                      如果有效负载为空，则使用空字符串作为哈希函数的输入。
 */
public record CanonicalRequest(String method,
                               String uri,
                               String queryString,
                               String headers,
                               String signedHeaders,
                               String hashedPayload) {
    public String toPlainText() {
        return method + "\n"
                + uri + "\n"
                + queryString + "\n"
                + headers + "\n"
                + signedHeaders + "\n"
                + hashedPayload;
    }

}
