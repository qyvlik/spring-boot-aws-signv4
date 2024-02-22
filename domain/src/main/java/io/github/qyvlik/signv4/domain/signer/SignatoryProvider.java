package io.github.qyvlik.signv4.domain.signer;

public interface SignatoryProvider {
    Signatory getSignatory(String accessKey);
}
