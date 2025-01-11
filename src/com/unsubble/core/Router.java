package com.unsubble.core;

import com.unsubble.models.HttpMethod;
import com.unsubble.models.HttpResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Router {

    private final Map<String, Map<HttpMethod, Class<?>>> routes;
    private final Path mainStaticAssetsPackage;
    private final Map<String, Path> staticAssetsPackageMap;

    public Router(List<Path> assetsPackages) {
        routes = new ConcurrentHashMap<>();
        staticAssetsPackageMap = new HashMap<>();
        mainStaticAssetsPackage = assetsPackages.getLast();
        resolveAssetsPackages(assetsPackages);
    }

    public void register(String path, HttpMethod method, Class<?> handler) {
        routes.computeIfAbsent(path, k -> new ConcurrentHashMap<>()).put(method, handler);
    }

    @SuppressWarnings("unchecked")
    public Class<AbstractHandler> getHandler(String path, HttpMethod method) {
        return (Class<AbstractHandler>) routes.getOrDefault(path, Collections.emptyMap()).get(method);
    }

    private void resolveAssetsPackages(List<Path> assetsPackages) {
        for (Path assetsPackage : assetsPackages) {
            try (Stream<Path> paths = Files.walk(assetsPackage, 1)) {
                paths.filter(Files::isRegularFile).forEach(p ->
                    staticAssetsPackageMap.putIfAbsent(HttpServer.ROOT.resolve(assetsPackage.relativize(p)).toString(), p)
                );
            } catch (IOException e) {
                // Do nothing...
            }
        }
    }

    public Path getDefaultStaticAssetsPackage(String path) {
        return staticAssetsPackageMap.getOrDefault(path, getMainStaticAssetsPackage());
    }

    public Path getMainStaticAssetsPackage() {
        return mainStaticAssetsPackage;
    }
}