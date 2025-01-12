package com.unsubble.core;

import com.unsubble.handlers.StaticFileHandler;
import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;
import com.unsubble.models.HttpStatus;
import com.unsubble.utils.ReflectionUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ConnectionHandler.class.getName());
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
            HttpResponse response = handleRequest(request);
            out.write(response.toString().getBytes());
            out.flush();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error handling connection", e);
        }
    }

    private HttpResponse handleRequest(HttpRequest request) {
        try {
            Class<AbstractHandler> handlerClass = router.getHandler(request.getPath(), request.getMethod());
            if (handlerClass == null) {
                return handleStaticFile(request.getPath());
            } else {
                return handleDynamicRequest(handlerClass, request);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error handling request", e);
            return createErrorResponse();
        }
    }

    private HttpResponse handleStaticFile(String path) {
        StaticFileHandler fileHandler = new StaticFileHandler(router.getMainStaticAssetsPackage().toString());
        return fileHandler.handleRequest(router.getDefaultStaticAssetsPackage(path).toString());
    }

    private HttpResponse handleDynamicRequest(Class<AbstractHandler> handlerClass, HttpRequest request) {
        AbstractHandler handler = (AbstractHandler) ReflectionUtil.newInstanceWithEmptyConstructor(handlerClass);
        handler.initializeRouter(router);
        return handler.handle(request);
    }

    private HttpResponse createErrorResponse() {
        return new HttpResponseBuilder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error")
                .build();
    }
}