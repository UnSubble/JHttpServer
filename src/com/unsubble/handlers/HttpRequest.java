package com.unsubble.handlers;

import java.util.List;
import java.util.Map;

public class HttpRequest {

    private HttpMethod method;
    private String path;
    private String version;
    private List<HttpHeader> headers;
    private String body;

    protected void setMethod(HttpMethod method) {
        this.method = method;
    }

    protected void setPath(String path) {
        this.path = path;
    }

    protected void setVersion(String version) {
        this.version = version;
    }

    protected void setHeaders(List<HttpHeader> headers) {
        this.headers = headers;
    }

    protected void setBody(String body) {
        this.body = body;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getPath() {
        return path;
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

    public String getHeaderValue(String key) {
        return headers.stream()
                .filter(x -> x.key().equals(key))
                .findFirst()
                .orElse(new HttpHeader(null, null))
                .value();
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "method=" + method +
                ", path='" + path + '\'' +
                ", version='" + version + '\'' +
                ", headers=" + headers +
                ", body='" + body + '\'' +
                '}';
    }

}
