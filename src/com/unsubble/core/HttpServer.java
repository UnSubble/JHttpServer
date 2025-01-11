package com.unsubble.core;

import com.unsubble.handlers.ConfigHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;

public class HttpServer {
    private final int port;
    private final Path[] configPaths;

    public HttpServer(Path[] configPaths, int port) {
        this.port = port;
        if (configPaths == null)
            configPaths = new Path[0];
        this.configPaths = configPaths;
    }

    public HttpServer(int port) {
        this(null, port);
    }

    public HttpServer() {
        this(null, 4444);
    }

    public void start() {
        handleConfigFiles();
        deadLoop();
    }

    private void handleConfigFiles() {
        ConfigHandler configHandler = new ConfigHandler(configPaths);
        try {
            configHandler.processConfigFiles();
        } catch (IOException | SAXException e) {
            throw new RuntimeException(e);
        }
    }

    private void deadLoop() {
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket client = socket.accept();
                client.setSoTimeout(5000);
                new Thread(new ConnectionHandler(client)).start();
            }
        } catch (IOException e) {
            // Do nothing...
        }
    }

    public int getPort() {
        return port;
    }
}
