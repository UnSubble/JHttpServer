package com.unsubble.core;

import com.unsubble.handlers.HttpHeader;
import com.unsubble.handlers.HttpResponse;
import com.unsubble.handlers.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class HttpResponseBuilder {
    private int statusCode;
    private String reasonPhrase;
    private String version = "HTTP/1.1";
    private final List<HttpHeader> headers = new ArrayList<>();
    private String body;

    public HttpResponseBuilder() {
    }

    public HttpResponseBuilder status(HttpStatus status) {
        this.statusCode = status.getCode();
        this.reasonPhrase = status.getReasonPhrase();
        return this;
    }

    public HttpResponseBuilder version(String version) {
        this.version = version;
        return this;
    }

    public HttpResponseBuilder addHeader(String name, String value) {
        headers.add(new HttpHeader(name, value));
        return this;
    }

    public HttpResponseBuilder body(String body) {
        this.body = body;
        return this;
    }

    public HttpResponse build() {
        if (body != null && !body.isEmpty()) {
            addHeader("Content-Length", String.valueOf(body.getBytes().length));
        }

        return new HttpResponse(statusCode, reasonPhrase, version, headers, body);
    }
}

