package io.github.qyvlik.signv4.server.gateway.action;

import io.github.qyvlik.domain.Result;
import io.github.qyvlik.signv4.server.gateway.action.request.ComplexJsonReq;
import io.github.qyvlik.signv4.server.gateway.action.request.PostRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
public class ApiController {

    @PostMapping(
            value = "api/v1/post-form",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result<String> echoPostForm(@RequestParam("param1") String param1, @RequestParam("param2") String param2) {
        return Result.success("param1: " + param1 + ", param2: " + param2);
    }

    @PostMapping(value = "api/v1/post-form-2",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result<String> echoPostForm2(PostRequest postRequest, @RequestParam("param3") String param3) {
        return Result.success("param1: " + postRequest.getParam1()
                + ", param2: " + postRequest.getParam2() + ", param3: " + param3);
    }

    @PutMapping(value = "api/v1/put-form",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public Result<String> echoPutForm(@RequestParam("param1") String param1,
                                      @RequestParam("param2") String param2,
                                      @RequestParam("param3") String param3) {
        return Result.success("param1: " + param1 + ", param2: " + param2 + ", param3: " + param3);
    }

    @DeleteMapping("api/v1/delete-form")
    public Result<String> echoDeleteForm(@RequestParam("param1") String param1,
                                         @RequestParam("param2") String param2) {
        return Result.success("param1: " + param1 + ", param2: " + param2);
    }

    @PostMapping("api/v1/post-json")
    public Result<String> echoPostJSON(@RequestBody PostRequest postRequest, @RequestParam("param3")  String param3) {
        return Result.success("param1: " + postRequest.getParam1()
                + ", param2: " + postRequest.getParam2() + ", param3: " + param3);
    }

    @DeleteMapping("api/v1/delete-json")
    public Result<String> echoDeleteJSON(@RequestBody PostRequest postRequest, @RequestParam("param3")  String param3) {
        return Result.success("param1: " + postRequest.getParam1()
                + ", param2: " + postRequest.getParam2() + ", param3: " + param3);
    }

    @PutMapping("api/v1/put-json")
    public Result<String> echoPutJSON(@RequestBody PostRequest postRequest, @RequestParam("param3")  String param3) {
        return Result.success("param1: " + postRequest.getParam1()
                + ", param2: " + postRequest.getParam2() + ", param3: " + param3);
    }

    @PostMapping("api/v1/post-json-2")
    public Result<String> echoPostJSON2(@RequestBody ComplexJsonReq postRequest, @RequestParam("param3")  String param3) {
        return Result.success("param1: " + postRequest.getParam1()
                + ", param2: " + postRequest.getList().get(0) + ", param3: " + param3);
    }
}
