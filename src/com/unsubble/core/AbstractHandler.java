package com.unsubble.core;

import com.unsubble.handlers.StaticFileHandler;
import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;
import com.unsubble.utils.ReflectionUtil;

public abstract class AbstractHandler implements Handler {

    private Router router;

    final void initializeRouter(Router router) {
        this.router = router;
    }

    public final HttpResponse dispatch(String newPath, HttpRequest request) {
        Class<AbstractHandler> handlerClass = router.getHandler(newPath, request.getMethod());
        HttpResponse response;
        if (handlerClass == null) {
            StaticFileHandler fileHandler = new StaticFileHandler(router.getMainStaticAssetsPackage().toString());
            response = fileHandler.handleRequest(router.getDefaultStaticAssetsPackage(newPath).toString());
        } else {
            Handler handler = (Handler) ReflectionUtil.newInstanceWithEmptyConstructor(handlerClass);
            response = handler.handle(request);
        }
        return response;
    }

}
