package com.unsubble.core;

import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;
import com.unsubble.handlers.StaticFileHandler;
import com.unsubble.utils.ReflectionUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket clientSocket;
    private final Router router;

    public ConnectionHandler(Socket clientSocket, Router router) {
        this.clientSocket = clientSocket;
        this.router = router;
    }

    @Override
    public void run() {
        try (InputStream in = clientSocket.getInputStream();
             OutputStream out = clientSocket.getOutputStream()) {
            HttpRequest request = new HttpRequestParser().parseWithStream(in);

            Class<AbstractHandler> handlerClass = router.getHandler(request.getPath(), request.getMethod());
            HttpResponse response;

            if (handlerClass == null) {
                StaticFileHandler fileHandler = new StaticFileHandler(router.getMainStaticAssetsPackage().toString());
                response = fileHandler.handleRequest(
                        router.getDefaultStaticAssetsPackage(request.getPath()).resolve(request.getPath()).toString());
            } else {
                AbstractHandler handler = (AbstractHandler) ReflectionUtil.newInstanceWithEmptyConstructor(handlerClass);
                handler.initializeRouter(router);
                response = handler.handle(request);
            }

            out.write(response.toString().getBytes());
            out.flush();
        } catch (IOException e) {
        }
    }
}