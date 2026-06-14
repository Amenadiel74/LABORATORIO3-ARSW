import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.List;

public class SalonHttpServer {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);
        SalonRepository repository = new SalonRepository();
        
        server.createContext("/rooms", new RoomsHandler(repository));
        server.createContext("/rooms/reserve", new ReserveHandler(repository));
        server.createContext("/rooms/release", new ReleaseHandler(repository));
        
        server.setExecutor(null);
        server.start();
        System.out.println("SalonHttpServer escuchando en http://localhost:8081/rooms");
    }

    private static String extractId(String query) {
        if (query == null || !query.startsWith("id=")) return null;
        return query.substring(3);
    }

    static class RoomsHandler implements HttpHandler {
        private SalonRepository repository;

        public RoomsHandler(SalonRepository repository) {
            this.repository = repository;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                if (!"GET".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }
                
                String query = exchange.getRequestURI().getQuery();
                String response;
                int statusCode = 200;
                
                if (query == null) {
                    List<Salon> salones = repository.findAll();
                    StringBuilder sb = new StringBuilder();
                    for (Salon s : salones) {
                        sb.append(s.getCodigo()).append(": ").append(s.getEstado()).append("\n");
                    }
                    response = sb.toString();
                } else {
                    String id = extractId(query);
                    String result = repository.consultarSalon(id);
                    if (result.equals("ERROR_SALON_NO_EXISTE")) {
                        statusCode = 404;
                    }
                    response = result + "\n";
                }
                
                exchange.sendResponseHeaders(statusCode, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ReserveHandler implements HttpHandler {
        private SalonRepository repository;

        public ReserveHandler(SalonRepository repository) {
            this.repository = repository;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }
                
                String query = exchange.getRequestURI().getQuery();
                String id = extractId(query);
                
                if (id == null) {
                    String error = "ERROR_OPERACION_INVALIDA\n";
                    exchange.sendResponseHeaders(400, error.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(error.getBytes());
                    os.close();
                    return;
                }
                
                String result = repository.reservarSalon(id);
                int statusCode = 200;
                if (result.equals("ERROR_SALON_NO_EXISTE")) statusCode = 404;
                else if (result.equals("ERROR_OPERACION_INVALIDA")) statusCode = 400;
                
                String response = result + "\n";
                exchange.sendResponseHeaders(statusCode, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static class ReleaseHandler implements HttpHandler {
        private SalonRepository repository;

        public ReleaseHandler(SalonRepository repository) {
            this.repository = repository;
        }

        @Override
        public void handle(HttpExchange exchange) {
            try {
                if (!"POST".equals(exchange.getRequestMethod())) {
                    exchange.sendResponseHeaders(405, -1);
                    return;
                }
                
                String query = exchange.getRequestURI().getQuery();
                String id = extractId(query);
                
                if (id == null) {
                    String error = "ERROR_OPERACION_INVALIDA\n";
                    exchange.sendResponseHeaders(400, error.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(error.getBytes());
                    os.close();
                    return;
                }
                
                String result = repository.liberarSalon(id);
                int statusCode = 200;
                if (result.equals("ERROR_SALON_NO_EXISTE")) statusCode = 404;
                
                String response = result + "\n";
                exchange.sendResponseHeaders(statusCode, response.getBytes().length);
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
