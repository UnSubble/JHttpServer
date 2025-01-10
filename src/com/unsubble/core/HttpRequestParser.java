package com.unsubble.core;

import com.unsubble.handlers.HttpMethod;
import com.unsubble.handlers.HttpRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class HttpRequestParser {
    private static final int BUFFER_SIZE = 8192; // 8 KB

    public HttpRequest parseWithStream(InputStream in) {
        byte[] tempBuffer = new byte[BUFFER_SIZE];
        int sizeRead;

        ByteArrayOutputStream bufferStream = new ByteArrayOutputStream();

        try (in) {
            while ((sizeRead = in.read(tempBuffer)) != -1) {
                bufferStream.write(tempBuffer, 0, sizeRead);
            }
            return parse(bufferStream.toString(StandardCharsets.UTF_8));
        } catch (IOException e) {
        }
        return null;
    }

    private void handleStartLine(HttpRequestImpl requestObj, String line) {
        String[] cnt = line.split("\\s+");
        HttpMethod method = HttpMethod.fromString(cnt[0]);
        requestObj.setMethod(method);
        requestObj.setPath(cnt[1]);
        requestObj.setVersion(cnt[2]);
    }

    public HttpRequest parse(String content) {
        HttpRequestImpl requestObj = new HttpRequestImpl();
        String[] lines = content.split("\\s*\r\n");
        String startLine = lines[0];
        handleStartLine(requestObj, startLine);
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
        protected void setVersion(String v) {
            super.setVersion(v);
        }
    }
}
