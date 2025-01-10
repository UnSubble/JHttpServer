package com.unsubble.core;

import com.unsubble.handlers.HttpRequest;
import com.unsubble.handlers.HttpResponse;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket clientSocket;

    public ConnectionHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (InputStream in = clientSocket.getInputStream();
             OutputStream out = clientSocket.getOutputStream()) {
            HttpRequest request = new HttpRequestParser().parseWithStream(in);
            HttpResponse response = new HttpResponseBuilder().buildResponse(request);

            out.write(response.toBytes());
            out.flush();
        } catch (IOException e) {
        }
    }
}
