package com.unsubble.core;

import com.unsubble.models.HttpMethod;

public final class RequestHandlerMethodFactory {

    public static HttpMethodHandler getHandler(HttpMethod method) {
        return switch (method) {
            case POST -> new PostHandler();
            case PATCH -> new PatchHandler();
            case GET, DELETE, PUT, OPTIONS -> null;
            default -> throw new UnsupportedOperationException("Unsupported HTTP Method: " + method);
        };
    }
}
