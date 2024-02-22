package io.github.qyvlik.signv4.web.interceptor;

import io.github.qyvlik.signv4.domain.auth.AuthResult;
import io.github.qyvlik.signv4.domain.auth.AuthState;
import io.github.qyvlik.signv4.domain.auth.Authenticating;
import io.github.qyvlik.signv4.domain.model.RequestData;
import io.github.qyvlik.signv4.web.context.ReqContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j
public class AwsSignV4AuthInterceptor implements HandlerInterceptor {

    private final Authenticating authenticating;

    public AwsSignV4AuthInterceptor(final Authenticating authenticating) {
        this.authenticating = authenticating;
    }

    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        RequestData requestData = RequestDataUtils.create(request);
        AuthResult authResult = authenticating.auth(requestData);

        if (!authResult.success()) {
            log.info("server signing={}", authResult.signing());

            writeResp(response, authResult.state().status);
        } else {
            String ak = requestData.authorization().credential().accessKey();
//             fill some info
            ReqContext.add(requestData);
        }
        return authResult.success();
//        return true;
    }

    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                @Nullable Exception ex) throws Exception {
        ReqContext.remove();
    }


    public static void writeResp(HttpServletResponse response, int status) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.setStatus(status);
        PrintWriter writer = response.getWriter();
        writer.append("");
    }
}
