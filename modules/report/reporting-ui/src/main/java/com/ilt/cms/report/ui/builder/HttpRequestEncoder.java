package com.ilt.cms.report.ui.builder;

import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class HttpRequestEncoder {

    private String prefix;
    private Map<String, Object> params;

    public HttpRequestEncoder(String url) {
        prefix = url + "?";
        params = new HashMap<>();
    }

    public HttpRequestEncoder param(String param, Object value) {
        params.put(param, value);
        return this;
    }

    @Override
    public String toString() {
        return params.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(joining("&", prefix, ""));
    }


}
