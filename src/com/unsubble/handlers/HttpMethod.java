package com.unsubble.handlers;

import java.util.NoSuchElementException;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE;

    public static HttpMethod fromString(String methodStr) {
        methodStr = methodStr.trim();
        for (HttpMethod method : HttpMethod.values()) {
            if (method.toString().equalsIgnoreCase(methodStr))
                return method;
        }
        throw new NoSuchElementException();
    }

    @Override
    public String toString() {
        return switch (this) {
            case GET -> "GET";
            case POST -> "POST";
            case PUT -> "PUT";
            case DELETE -> "DELETE";
        };
    }
}
