package io.github.qyvlik.signv4.domain.auth;

import io.github.qyvlik.signv4.domain.signer.Signatory;
import io.github.qyvlik.signv4.domain.model.Authorization;
import io.github.qyvlik.signv4.domain.model.RequestData;
import io.github.qyvlik.signv4.domain.model.Signing;
import io.github.qyvlik.signv4.domain.signer.SignatoryProvider;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.TimeZone;

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
        final long now = System.currentTimeMillis();
        String requestDateTime = requestData.requestDateTime();
        Long requestTime = parseRequestDateTime(requestDateTime);
        if (requestTime == null || Math.abs(now - requestTime) > this.requestTimeout) {
            return new AuthResult(null, AuthState.REQUEST_TIMEOUT);
        }
        Signatory signatory = this.signatoryProvider.getSignatory(authorization.credential().accessKey());
        if (signatory == null) {
            return new AuthResult(null, AuthState.SECRET_KEY_NOT_FOUND);
        }
        Signing signing = requestData.signature(signatory);
        if (!signatureFromClient.equals(signing.signature())) {
            return new AuthResult(signing, AuthState.SIGNATURE_INVALIDATE);
        }
        return new AuthResult(signing, AuthState.SUCCESS);
    }

    public static Long parseRequestDateTime(String requestDateTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(requestDateTime).getTime();
        } catch (ParseException e) {
            return null;
        }
    }

}
