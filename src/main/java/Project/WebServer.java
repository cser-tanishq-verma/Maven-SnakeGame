package Project;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class WebServer {

    public static void main(String[] args) throws IOException {
        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/", new StaticHandler());
        server.setExecutor(Executors.newFixedThreadPool(4));
        server.start();

        System.out.println("Snake game available at http://localhost:" + port);
    }

    private static class StaticHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            String resourcePath;

            if ("/".equals(requestPath) || "".equals(requestPath)) {
                resourcePath = "/static/index.html";
            } else {
                resourcePath = "/static" + requestPath;
            }

            if (resourcePath.endsWith("/")) {
                resourcePath += "index.html";
            }

            InputStream resourceStream = getClass().getResourceAsStream(resourcePath);
            if (resourceStream == null) {
                byte[] notFound = "404 Not Found".getBytes(StandardCharsets.UTF_8);
                exchange.sendResponseHeaders(404, notFound.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(notFound);
                }
                return;
            }

            String contentType = URLConnection.guessContentTypeFromName(resourcePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            byte[] body = resourceStream.readAllBytes();
            exchange.getResponseHeaders().set("Content-Type", contentType + "; charset=UTF-8");
            exchange.sendResponseHeaders(200, body.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(body);
            }
        }
    }
}
