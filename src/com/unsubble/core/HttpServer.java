package com.unsubble.core;

import com.unsubble.handlers.CommonHandler;
import com.unsubble.handlers.ConfigHandler;
import com.unsubble.models.HttpMethod;
import com.unsubble.utils.ReflectionUtil;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Path;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpServer {

    private static final Logger LOGGER = Logger.getLogger(HttpServer.class.getName());
    public static final Path ASSETS_PATH = Path.of("src/com/unsubble/assets").toAbsolutePath();
    public static final Path ROOT = Path.of("/");
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
        ConfigHandler handler = handleConfigFiles();

        List<Path> assetsPackages = handler.getAssetsPackages();
        assetsPackages.add(ASSETS_PATH);
        List<Class<?>> requestHandlerClasses = handler.getRequestHandlerClasses();

        Router router = new Router(assetsPackages);
        registerExternalHandlers(router, requestHandlerClasses);
        registerCommons(router);

        deadLoop(router);
    }

    private void registerExternalHandlers(Router router, List<Class<?>> requestHandlerClasses) {
        for (Class<?> requestHandlerClass : requestHandlerClasses) {
            if (ReflectionUtil.suits(AbstractHandler.class, requestHandlerClass)
                    && ReflectionUtil.isAnnotationPresent(requestHandlerClass, RequestHandler.class)) {
                RequestHandler requestHandlerAnnotation = requestHandlerClass.getAnnotation(RequestHandler.class);
                String path = requestHandlerAnnotation.value();
                HttpMethod[] methods = requestHandlerAnnotation.supportedMethods();
                for (HttpMethod method : methods) {
                    router.register(path, method, requestHandlerClass);
                }
            }
        }
    }

    private void registerCommons(Router router) {
        router.register("/", HttpMethod.GET, CommonHandler.class);
        router.register("/", HttpMethod.POST, CommonHandler.class);
        router.register("/", HttpMethod.PUT, CommonHandler.class);
        router.register("/", HttpMethod.DELETE, CommonHandler.class);
        router.register("/*", HttpMethod.OPTIONS, CommonHandler.class);
    }

    private ConfigHandler handleConfigFiles() {
        ConfigHandler configHandler = new ConfigHandler(configPaths);
        try {
            configHandler.handleConfigFiles();
        } catch (IOException | SAXException e) {
            LOGGER.log(Level.SEVERE, "An error occurred when handling given config files.", e);
            throw new RuntimeException(e);
        }
        return configHandler;
    }

    private void deadLoop(Router router) {
        try (ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                Socket client = socket.accept();
                client.setSoTimeout(5000);
                new Thread(new ConnectionHandler(client, router)).start();
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error in server socket", e);
        }
    }

    public int getPort() {
        return port;
    }
}