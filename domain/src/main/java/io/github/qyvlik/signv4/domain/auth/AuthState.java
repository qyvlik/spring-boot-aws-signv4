package io.github.qyvlik.signv4.domain.auth;

public enum AuthState {
    SUCCESS(200),

    SIGNATURE_IS_BLANK(403),

    SECRET_KEY_NOT_FOUND(403),

    REQUEST_TIMEOUT(408),

    SIGNATURE_INVALIDATE(403);

    AuthState(int status) {
        this.status = status;
    }

    public final int status;

    public boolean success() {
        return status == 200;
    }
}
