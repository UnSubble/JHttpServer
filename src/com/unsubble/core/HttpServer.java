package com.unsubble.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HttpServer {
    private final int port;

    public HttpServer(int port) {
        this.port = port;
    }

    public HttpServer() {
        this(4444);
    }

    public void start() {
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket client = socket.accept();
                client.setSoTimeout(5000);
                new Thread(new ConnectionHandler(client)).start();
            }
        } catch (IOException e) {
        }
    }

    public int getPort() {
        return port;
    }
}
