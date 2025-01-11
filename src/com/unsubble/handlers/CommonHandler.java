package com.unsubble.handlers;

import com.unsubble.core.AbstractHandler;
import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;

public class CommonHandler extends AbstractHandler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        return this.dispatch("/index.html", request);
    }
}
