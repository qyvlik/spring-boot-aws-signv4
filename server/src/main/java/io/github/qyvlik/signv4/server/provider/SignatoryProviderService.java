package io.github.qyvlik.signv4.server.provider;

import com.google.common.collect.Maps;
import io.github.qyvlik.signv4.domain.auth.SignatoryProvider;
import io.github.qyvlik.signv4.domain.hash.LocalSignatory;
import io.github.qyvlik.signv4.domain.hash.Signatory;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class SignatoryProviderService implements SignatoryProvider {

    private final ConcurrentMap<String, String> keys;

    public SignatoryProviderService(Map<String, String> keys) {
        this.keys = Maps.newConcurrentMap();
        this.keys.putAll(keys);
    }

    @Override
    public Signatory getSignatory(String accessKey) {
        String secretKey = keys.get(accessKey);
        if (StringUtils.isBlank(secretKey)) {
            return null;
        }
        return LocalSignatory.create(secretKey);
    }
}
