package com.cardinity.model;

public class Response {

    private final Integer code;
    private final String body;

    public Response(Integer code, String body) {
        this.code = code;
        this.body = body;
    }

    public Integer getCode() {
        return code;
    }

    public String getBody() {
        return body;
    }
}
