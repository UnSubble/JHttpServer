package com.unsubble.core;

import com.unsubble.models.HttpHeader;
import com.unsubble.models.HttpMethod;
import com.unsubble.models.HttpRequest;

import java.util.List;
import java.util.Map;

public class HttpRequestImpl extends HttpRequest {

    @Override
    public void setMethod(HttpMethod method) {
        super.setMethod(method);
    }

    @Override
    public void setPath(String path) {
        super.setPath(path);
    }

    @Override
    protected void setVersion(String version) {
        super.setVersion(version);
    }

    @Override
    public void setHeaders(List<HttpHeader> headers) {
        super.setHeaders(headers);
    }

    @Override
    public void setBody(String body) {
        super.setBody(body);
    }

    @Override
    public void setParameters(Map<String, String> parameters) {
        super.setParameters(parameters);
    }
}