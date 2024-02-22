package io.github.qyvlik.signv4.server.gateway.action;

import io.github.qyvlik.domain.Result;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleController {

    @PostMapping(
            value = "front/v1/post-form",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result<String> echoPostForm(String param1, String param2) {
        return Result.success("front/v1/post-form: param1: " + param1 + ", param2: " + param2);
    }
}
