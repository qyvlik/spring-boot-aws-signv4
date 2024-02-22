package io.github.qyvlik.signv4.domain.hash;

import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class LocalSignatory implements Signatory {
    private final String secretKey;

    private LocalSignatory(String secretKey) {
        this.secretKey = secretKey;
    }

    public static LocalSignatory create(String secretKey) {
        return new LocalSignatory(secretKey);
    }

    @Override
    public byte[] signature(byte[] message) {
        return Hashing.hmacSha256(("AWS4" + secretKey).getBytes(StandardCharsets.UTF_8))
                .hashBytes(message).asBytes();
    }
}
