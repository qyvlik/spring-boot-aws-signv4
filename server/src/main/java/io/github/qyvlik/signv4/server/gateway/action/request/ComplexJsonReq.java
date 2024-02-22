package io.github.qyvlik.signv4.server.gateway.action.request;

import lombok.Data;

import java.util.List;

@Data
public class ComplexJsonReq {
    private String param1;
    private List<String> list;
}
