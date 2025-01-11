package com.unsubble.handlers;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler {

    private final Path[] configPaths;
    private final DocumentBuilder documentBuilder;
    private List<Path> assetsPackages;
    private List<Class<?>> requestHandlerClasses;

    public ConfigHandler(Path[] configPaths) {
        this.configPaths = configPaths;

        try {
            checkPathExistence();
        } catch (NoSuchFileException e) {
            throw new RuntimeException(e);
        }

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        try {
            documentBuilder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        }

        assetsPackages = null;
        requestHandlerClasses = null;
    }

    private void checkPathExistence() throws NoSuchFileException {
        for (Path cfgPath : configPaths) {
            if (Files.notExists(cfgPath) || !Files.isRegularFile(cfgPath)) {
                throw new NoSuchFileException(cfgPath.toString());
            }
            if (!cfgPath.getFileName().toString().endsWith(".xml")) {
                throw new UnsupportedOperationException();
            }
        }
    }

    public List<Path> getAssetsPackages() {
        if (assetsPackages == null) {
            try {
                handleConfigFiles();
            } catch (IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        }
        return assetsPackages;
    }

    public List<Class<?>> getRequestHandlerClasses() {
        if (assetsPackages == null) {
            try {
                handleConfigFiles();
            } catch (IOException | SAXException e) {
                throw new RuntimeException(e);
            }
        }
        return requestHandlerClasses;
    }

    public void handleConfigFiles() throws IOException, SAXException {
        assetsPackages = new ArrayList<>();
        requestHandlerClasses = new ArrayList<>();

        for (Path cfgPath : configPaths) {
            Document document = documentBuilder.parse(cfgPath.toAbsolutePath().toFile());

            if (document.getDocumentElement().getNodeName().equalsIgnoreCase("configuration")) {
                NodeList assetsNodeList = document.getElementsByTagName("assets");

                if (assetsNodeList.getLength() > 0) {
                    addFoundPackages(assetsNodeList);
                }

                NodeList requestHandlersClasses = document.getElementsByTagName("request-handler");

                if (requestHandlersClasses.getLength() > 0) {
                    addFoundClasses(requestHandlersClasses);
                }
            }
        }
    }

    private void addFoundClasses(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            String requestHandlerClassStr = list.item(i).getTextContent();

            try {
                Class<?> requestHandlerClass = Class.forName(requestHandlerClassStr);
                requestHandlerClasses.add(requestHandlerClass);
            } catch (ClassNotFoundException e) {
                // Do nothing...
            }

        }
    }

    private void addFoundPackages(NodeList list) {
        for (int i = 0; i < list.getLength(); i++) {
            String assetsPackagePathStr = replaceSlashes(list.item(i).getTextContent());
            Path assetsPackagePath = Path.of(assetsPackagePathStr).toAbsolutePath();

            if (Files.notExists(assetsPackagePath) || !Files.isDirectory(assetsPackagePath)) {
                continue;
            }

            assetsPackages.add(assetsPackagePath);
        }
    }

    private String replaceSlashes(String pathStr) {
        return pathStr.replace('.', '/');
    }
}
