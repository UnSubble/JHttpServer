package com.unsubble.core;

import com.unsubble.handlers.StaticFileHandler;
import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;
import com.unsubble.models.HttpStatus;
import com.unsubble.utils.ReflectionUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractHandler implements Handler {

    private static final Logger LOGGER = Logger.getLogger(AbstractHandler.class.getName());
    private Router router;

    final void initializeRouter(Router router) {
        this.router = router;
    }

    public final HttpResponse dispatch(String newPath, HttpRequest request) {
        try {
            Class<AbstractHandler> handlerClass = router.getHandler(newPath, request.getMethod());
            if (handlerClass == null) {
                return handleStaticFile(newPath);
            } else {
                return handleDynamicRequest(handlerClass, request);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error dispatching request", e);
            return createErrorResponse();
        }
    }

    private HttpResponse handleStaticFile(String newPath) {
        StaticFileHandler fileHandler = new StaticFileHandler(router.getMainStaticAssetsPackage().toString());
        return fileHandler.handleRequest(router.getDefaultStaticAssetsPackage(newPath).toString());
    }

    private HttpResponse handleDynamicRequest(Class<AbstractHandler> handlerClass, HttpRequest request) {
        Handler handler = (Handler) ReflectionUtil.newInstanceWithEmptyConstructor(handlerClass);
        return handler.handle(request);
    }

    private HttpResponse createErrorResponse() {
        return new HttpResponseBuilder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Internal Server Error")
                .build();
    }
}