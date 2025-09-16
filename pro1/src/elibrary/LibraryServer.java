package elibrary;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class LibraryServer {
    private static Library library = new Library();

    public static void main(String[] args) throws Exception {
        // Add default admin
        library.register("admin", "admin", Role.ADMIN);

        // Add some books
        library.addBook("Tales of Cities", "Nethu", 10);

        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);

        // GET all books
        server.createContext("/books", (HttpExchange exchange) -> {
            List<Book> books = library.getBooks();
            String json = books.stream()
                    .map(b -> String.format("{\"id\":%d,\"title\":\"%s\",\"author\":\"%s\",\"quantity\":%d}",
                            b.getId(), b.getTitle(), b.getAuthor(), b.getQuantity()))
                    .collect(Collectors.joining(",", "[", "]"));
            exchange.getResponseHeaders().add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, json.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.close();
        });

        // TODO: Add POST /register, /login, /borrow, /return later

        server.start();
        System.out.println("Server running at http://localhost:8000");
    }
}
