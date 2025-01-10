package com.unsubble.utils;

import com.unsubble.handlers.HttpHeader;

import java.util.List;

public final class ObjToString {

    private ObjToString() {
        throw new AssertionError();
    }

    public static String toString(String[] title, List<HttpHeader> headers, String... body) {
        StringBuilder buffer = new StringBuilder();
        for (String t : title) {
            buffer.append(t).append(" ");
        }
        buffer.deleteCharAt(buffer.length() - 1).append("\r\n");
        for (HttpHeader header : headers) {
            buffer.append(header.key()).append(": ").append(header.value()).append("\r\n");
        }
        buffer.append("\r\n");
        if (body != null) {
            for (String b : body) {
                buffer.append(b);
            }
        }
        return buffer.toString();
    }
}
