package io.github.qyvlik.signv4.domain.auth;

import io.github.qyvlik.signv4.domain.signer.Signing;

/**
 *
 * @param signing 签名过程
 * @param state   验证状态
 */
public record AuthResult(Signing signing, AuthState state) {
    public boolean success() {
        return state.success();
    }
}
