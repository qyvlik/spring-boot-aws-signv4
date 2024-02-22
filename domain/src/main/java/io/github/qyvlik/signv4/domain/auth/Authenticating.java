package io.github.qyvlik.signv4.domain.auth;

import io.github.qyvlik.signv4.domain.hash.Signatory;
import io.github.qyvlik.signv4.domain.model.Authorization;
import io.github.qyvlik.signv4.domain.model.RequestData;
import io.github.qyvlik.signv4.domain.model.Signing;
import org.apache.commons.lang3.StringUtils;

import java.time.Duration;
import java.util.function.BiConsumer;

public class Authenticating {
    private final SignatoryProvider signatoryProvider;
    private final long requestTimeout;

    public Authenticating(final SignatoryProvider signatoryProvider, final Duration requestTimeout) {
        this.signatoryProvider = signatoryProvider;
        this.requestTimeout = requestTimeout.toMillis();
    }

    public AuthResult auth(RequestData requestData) {
        Authorization authorization = requestData.authorization();
        String signatureFromClient = authorization.signature();
        if (StringUtils.isBlank(signatureFromClient)) {
            return new AuthResult(null, AuthState.SIGNATURE_IS_BLANK);
        }
        Signatory signatory = this.signatoryProvider.getSignatory(authorization.credential().accessKey());
        if (signatory == null) {
            return new AuthResult(null, AuthState.SECRET_KEY_NOT_FOUND);
        }
        final long now = System.currentTimeMillis();
        String requestDateTime = requestData.requestDateTime();
        Long requestTime = RequestData.parseRequestDateTime(requestDateTime);
        if (requestTime == null || Math.abs(now - requestTime) > this.requestTimeout) {
            return new AuthResult(null, AuthState.REQUEST_TIMEOUT);
        }
        Signing signing = requestData.signature(signatory);
        if (!signatureFromClient.equals(signing.signature())) {
            return new AuthResult(signing, AuthState.SIGNATURE_INVALIDATE);
        }
        return new AuthResult(signing, AuthState.SUCCESS);
    }

}
