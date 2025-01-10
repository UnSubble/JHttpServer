package com.unsubble.core;

import com.unsubble.handlers.HttpHeader;
import com.unsubble.handlers.HttpMethod;
import com.unsubble.handlers.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestParser {
    private static final int BUFFER_SIZE = 8192; // 8 KB

    public HttpRequest parseWithStream(InputStream in) throws IOException {
        ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();
        byte[] tempBuffer = new byte[BUFFER_SIZE];
        int sizeRead;

        HttpRequestImpl requestObj = new HttpRequestImpl();

        while ((sizeRead = in.read(tempBuffer)) != -1) {
            bufferStream.write(tempBuffer, 0, sizeRead);
            HttpRequest partialRequest = tryParse(bufferStream.toString(StandardCharsets.UTF_8));
            if (partialRequest != null) {
                mergeRequests(requestObj, partialRequest);
            }
        }

        if (requestObj.getHeaders().isEmpty() && requestObj.getMethod() == null) {
            throw new IOException("Incomplete HTTP request received");
        }

        return requestObj;
    }

    private void mergeRequests(HttpRequestImpl mainRequest, HttpRequest partialRequest) {
        if (partialRequest.getMethod() != null) {
            mainRequest.setMethod(partialRequest.getMethod());
        }

        if (partialRequest.getPath() != null) {
            mainRequest.setPath(partialRequest.getPath());
        }

        if (partialRequest.getVersion() != null) {
            mainRequest.setVersion(partialRequest.getVersion());
        }

        if (partialRequest.getHeaders() != null && !partialRequest.getHeaders().isEmpty()) {
            mainRequest.getHeaders().addAll(partialRequest.getHeaders());
        }

        if (partialRequest.getBody() != null) {
            mainRequest.setBody(partialRequest.getBody());
        }
    }


    private HttpRequest tryParse(String content) {
        try {
            HttpRequestImpl requestObj = new HttpRequestImpl();
            String[] lines = content.split("\r\n");

            handleStartLine(requestObj, lines);

            int headerEndIndex = handleHeaders(requestObj, lines);

            if (requiresBody(requestObj.getMethod())) {
                handleBodyIfAvailable(requestObj, content, headerEndIndex);
            }

            return requestObj;
        } catch (Exception e) {
        }
        return null;
    }

    private boolean requiresBody(HttpMethod method) {
        return method == HttpMethod.POST || method == HttpMethod.PUT;
    }

    private void handleBodyIfAvailable(HttpRequestImpl requestObj, String content, int headerEndIndex) {
        String contentLengthHeader = requestObj.getHeaderValue("Content-Length");
        if (contentLengthHeader != null) {
            int contentLength = Integer.parseInt(contentLengthHeader.trim());
            int bodyStartIndex = content.indexOf("\r\n\r\n") + 4;

            if (bodyStartIndex + contentLength <= content.length()) {
                String body = content.substring(bodyStartIndex, bodyStartIndex + contentLength);
                requestObj.setBody(body.trim());
            }
        }
    }

    private int handleHeaders(HttpRequestImpl requestObj, String[] lines) {
        List<HttpHeader> headers = new ArrayList<>();
        int index = 1;

        while (index < lines.length && !lines[index].isEmpty()) {
            int colonIndex = lines[index].indexOf(":");
            if (colonIndex > 0) {
                String key = lines[index].substring(0, colonIndex).trim();
                String value = lines[index].substring(colonIndex + 1).trim();
                headers.add(new HttpHeader(key, value));
            }
            index++;
        }

        requestObj.setHeaders(headers);
        return index + 1;
    }

    private void handleStartLine(HttpRequestImpl requestObj, String[] lines) {
        if (lines.length == 0 || lines[0].isEmpty()) {
            throw new IllegalArgumentException("Invalid HTTP request: Start line missing");
        }

        String[] parts = lines[0].split("\\s+");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Invalid HTTP start line");
        }

        requestObj.setMethod(HttpMethod.fromString(parts[0]));
        requestObj.setPath(parts[1]);
        requestObj.setVersion(parts[2]);
    }

    private static class HttpRequestImpl extends HttpRequest {
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
    }
}
