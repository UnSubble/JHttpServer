package com.unsubble.core;

import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;
import com.unsubble.handlers.StaticFileHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;

public class ConnectionHandler implements Runnable {

    private final Socket clientSocket;
    private static final Path ASSETS_PATH = Path.of("src/com/unsubble/assets").toAbsolutePath();

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream in = clientSocket.getInputStream();
             OutputStream out = clientSocket.getOutputStream()) {
            HttpRequest request = new HttpRequestParser().parseWithStream(in);

            StaticFileHandler fileHandler = new StaticFileHandler(ASSETS_PATH.toString());
            HttpResponse response  = fileHandler.handleRequest(
                    ASSETS_PATH.resolve(request.getPath()).toString());

            out.write(response.toString().getBytes());
            out.flush();
        } catch (IOException e) {
        }
    }
}