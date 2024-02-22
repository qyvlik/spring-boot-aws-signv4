package io.github.qyvlik.signv4.server.gateway.action;

import io.github.qyvlik.domain.Result;
import io.github.qyvlik.signv4.server.gateway.action.request.HeadRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j

@RestController
public class GetController {

    @GetMapping("api/v1/time")
    public Result<Long> getTime() {
        return Result.success(System.currentTimeMillis());
    }

    @RequestMapping(method = RequestMethod.HEAD, value = "api/v1/head")
    public void head(HeadRequest request) {
        int catch_here = 0;
        log.info("api/v1/head:{}", request);
    }
}
