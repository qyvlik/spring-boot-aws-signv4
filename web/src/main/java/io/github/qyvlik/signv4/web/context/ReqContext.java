package io.github.qyvlik.signv4.web.context;

import io.github.qyvlik.signv4.domain.model.RequestData;
import io.github.qyvlik.signv4.domain.signer.Signing;
import org.springframework.core.NamedThreadLocal;

public record ReqContext(RequestData data, Signing signing) {
    public static final ThreadLocal<ReqContext> THREAD_LOCAL = new NamedThreadLocal<>("req-context");

    public static void add(RequestData data, Signing signing) {
        THREAD_LOCAL.set(new ReqContext(data, signing));
    }
    public static void remove() {
        THREAD_LOCAL.remove();
    }

    public static ReqContext get() {
        return THREAD_LOCAL.get();
    }
}
