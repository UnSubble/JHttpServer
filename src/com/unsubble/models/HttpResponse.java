package com.unsubble.models;

import com.unsubble.utils.ObjToString;

import java.util.List;

public class HttpResponse {
    private final int statusCode;
    private final String reasonPhrase;
    private final String version;
    private final List<HttpHeader> headers;
    private final String body;
    public static final String VERSION = "HTTP/1.1";

    public HttpResponse(int statusCode, String reasonPhrase, String version, List<HttpHeader> headers, String body) {
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
        this.version = version;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getReasonPhrase() {
        return reasonPhrase;
    }

    public String getVersion() {
        return version;
    }

    public List<HttpHeader> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return ObjToString.toString(new String[] {version, String.valueOf(statusCode), reasonPhrase},
                headers, body);
    }
}
