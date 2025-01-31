package com.unsubble.core;

import com.unsubble.models.HttpRequest;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PostHandler implements HttpMethodHandler {

    @Override
    public void handleRequest(HttpRequest request) {
        if (request instanceof HttpRequestImpl impl) {
            handlePostRequest(impl);
        }
    }

    private void handlePostRequest(HttpRequestImpl request) {
        String contentType = request.getHeaderValue("Content-Type");

        if ("application/x-www-form-urlencoded".equals(contentType)) {
            String body = request.getBody();
            Map<String, String> formData = parseUrlEncodedBody(body);

            request.setParameters(formData);
            request.setBody("");
        } else {
            throw new UnsupportedOperationException("Unsupported Content-Type: " + contentType);
        }
    }

    private Map<String, String> parseUrlEncodedBody(String body) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8));
            }
        }
        return params;
    }
}
