package com.unsubble.core;

import com.unsubble.models.HttpRequest;
import com.unsubble.models.HttpResponse;

@FunctionalInterface
interface Handler {
    HttpResponse handle(HttpRequest request);
}
