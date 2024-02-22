package io.github.qyvlik.signv4.domain.signer;

/**
 * HMAC-SHA256 签名
 * 本地签名或者远程签名
 */
public interface Signatory {
    byte[] signature(byte[] message);
}
