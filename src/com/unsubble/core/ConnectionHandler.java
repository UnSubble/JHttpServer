package com.unsubble.core;

import com.unsubble.handlers.HttpRequest;
import com.unsubble.handlers.HttpResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

            

            HttpResponse response = new HttpResponseBuilder()
                    .build();

            out.write(response.toString().getBytes());
            out.flush();
        } catch (IOException e) {
        }
    }
}
