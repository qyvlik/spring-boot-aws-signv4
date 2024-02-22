package io.github.qyvlik.signv4.domain.auth;

import io.github.qyvlik.signv4.domain.hash.Signatory;

public interface SignatoryProvider {
    Signatory getSignatory(String accessKey);
}
