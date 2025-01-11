package com.unsubble.handlers;

import com.unsubble.core.HttpResponseBuilder;
import com.unsubble.models.HttpResponse;
import com.unsubble.models.HttpStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class StaticFileHandler {

    private final String baseDir;

    public StaticFileHandler(String baseDir) {
        this.baseDir = baseDir;
    }

    public HttpResponse handleRequest(String path) {
        File file = new File(baseDir + path);
        if (file.exists() && file.isFile()) {
            try {
                String content = Files.readString(file.toPath());
                String contentType = getContentType(file.getName());
                return new HttpResponseBuilder()
                        .status(HttpStatus.OK)
                        .version(HttpResponse.VERSION)
                        .addHeader("Content-Type", contentType)
                        .body(content)
                        .build();
            } catch (IOException e) {
                return createErrorResponse();
            }
        } else {
            return create404Response();
        }
    }

    private HttpResponse createErrorResponse() {
        String errorMessage = """
        <!DOCTYPE html>
        <html lang="en">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <title>500 - Internal Server Error</title>
        </head>
        <body>
            <h1>500 - Internal Server Error</h1>
            <p>Something went wrong on the server.</p>
        </body>
        </html>
        """;

        return new HttpResponseBuilder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .addHeader("Content-Type", "text/html")
                .version(HttpResponse.VERSION)
                .body(errorMessage)
                .build();
    }

    private String getContentType(String fileName) {
        if (fileName.endsWith(".html")) return "text/html";
        if (fileName.endsWith(".css")) return "text/css";
        if (fileName.endsWith(".js")) return "application/javascript";
        return "application/octet-stream";
    }

    private HttpResponse create404Response() {
        File file = new File(baseDir + "/404.html");
        try {
            String content = Files.readString(file.toPath());
            return new HttpResponseBuilder()
                    .status(HttpStatus.NOT_FOUND)
                    .version(HttpResponse.VERSION)
                    .addHeader("Content-Type", "text/html")
                    .body(content)
                    .build();
        } catch (IOException e) {
            return createErrorResponse();
        }
    }
}
