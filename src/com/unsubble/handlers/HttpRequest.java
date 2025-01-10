package com.unsubble.handlers;

import com.unsubble.utils.ObjToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private HttpMethod method;
    private String path;
    private String version;
    private List<HttpHeader> headers;
    private Map<String, String> parameters;
    private String body;

    protected HttpRequest() {
        headers = new ArrayList<>();
        parameters = new HashMap<>();
    }

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

    protected void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
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

    public String getParameter(String name) {
        return parameters.get(name);
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
        return ObjToString.toString(new String[] {method.toString(), path, version}, headers, body) +
                (parameters.isEmpty() ? "" : "\nParameters: " + parameters);
    }

}
