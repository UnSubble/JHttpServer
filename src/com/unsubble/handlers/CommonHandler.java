package com.unsubble.handlers;

import com.unsubble.core.AbstractHandler;
import com.unsubble.core.HttpResponseBuilder;
import com.unsubble.models.HttpMethod;
import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;
import com.unsubble.models.HttpStatus;

import java.util.Arrays;

public class CommonHandler extends AbstractHandler {

    @Override
    public HttpResponse handle(HttpRequest request) {
        if (request.getMethod() == HttpMethod.OPTIONS) {
            return handleOptions(request);
        }
        return this.dispatch("/index.html", request);
    }

    private HttpResponse handleOptions(HttpRequest request) {
            return new HttpResponseBuilder()
                    .status(HttpStatus.NO_CONTENT)
                    .addHeader("Allow", Arrays.toString(HttpMethod.values())
                                    .replace("[", "")
                                    .replace("]", ""))
                    .body("")
                    .build();
    }
}
