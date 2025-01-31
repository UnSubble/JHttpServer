package com.unsubble.core;

import com.unsubble.models.HttpRequest;

public interface HttpMethodHandler {
    void handleRequest(HttpRequest request);
}
