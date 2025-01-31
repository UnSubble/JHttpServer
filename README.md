# HTTP Server Project
This project is a simple HTTP server implemented in Java. It handles HTTP requests, serves static files, and supports dynamic request handling through custom handlers.  

## Project Structure
- **`src/com/unsubble/assets/`:** Contains static HTML files.
  - `404.html`: Custom 404 error page.
  - `index.html`: Home page.
- **`src/com/unsubble/core/`:** Core server components.
  - `AbstractHandler.java`: Base class for request handlers.
  - `ConnectionHandler.java`: Handles client connections.
  - `Handler.java`: Functional interface for request handlers.
  - `HttpRequestParser.java`: Parses HTTP requests.
  - `HttpResponseBuilder.java`: Builds HTTP responses.
  - `HttpServer.java`: Main server class.
  - `Main.java`: Entry point of the application.
  - `RequestHandler.java`: Annotation for request handlers.
  - `Router.java`: Manages routing of requests.
- **`src/com/unsubble/handlers/`:** Custom request handlers.
  - `CommonHandler.java`: Handles common requests.
  - `ConfigHandler.java`: Handles configuration files.
  - `StaticFileHandler.java`: Serves static files.
- **`src/com/unsubble/models/`:** HTTP request and response models.
  - `HttpHeader.java`: Represents an HTTP header.
  - `HttpMethod.java`: Enum for HTTP methods.
  - `HttpRequest.java`: Represents an HTTP request.
  - `HttpResponse.java`: Represents an HTTP response.
  - `HttpStatus.java`: Enum for HTTP status codes.
- **`src/com/unsubble/utils/`:** Utility classes.
  - `ObjToString.java`: Utility for converting objects to strings.
  - `ReflectionUtil.java`: Utility for reflection operations.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11 or higher  
- IntelliJ IDEA or any other Java IDE  

### Running the Server
1. Clone the repository.  
2. Open the project in your IDE.  
3. Run the `Main` class located in `src/com/unsubble/core/Main.java`.  
4. The server will start on port 4444 by default.  

### Configuration
You can provide configuration files in XML format to customize the server. Place the configuration files in the project directory and specify their paths when creating the `HttpServer` instance.  

### Custom Handlers
To add custom request handlers:  
1. Create a new class that extends `AbstractHandler`.  
2. Annotate the class with `@RequestHandler` and specify the path and supported methods.  
3. Implement the `handle` method to process the request.  

#### Example:
```java
@RequestHandler(value = "/custom", supportedMethods = {HttpMethod.GET})
public class CustomHandler extends AbstractHandler {
    @Override
    public HttpResponse handle(HttpRequest request) {
        return new HttpResponseBuilder()
                .status(HttpStatus.OK)
                .body("Custom handler response")
                .build();
    }
}
```

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
