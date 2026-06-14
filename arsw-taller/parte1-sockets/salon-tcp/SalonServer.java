import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class SalonServer {
    public static void main(String[] args) throws Exception {
        SalonRepository repository = new SalonRepository();
        ServerSocket serverSocket = new ServerSocket(35001);
        System.out.println("SalonServer TCP escuchando en puerto 35001...");

        while (true) {
            Socket clientSocket = serverSocket.accept();
            new Thread(() -> handleClient(clientSocket, repository)).start();
        }
    }

    private static void handleClient(Socket clientSocket, SalonRepository repository) {
        try {
            BufferedReader in = new BufferedReader(
                new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String request = in.readLine();
            String response = processRequest(request, repository);
            out.println(response);

            in.close();
            out.close();
            clientSocket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String request, SalonRepository repository) {
        if (request == null) return "ERROR_OPERACION_INVALIDA";
        String[] parts = request.split(",");
        if (parts.length != 2) return "ERROR_OPERACION_INVALIDA";
        
        String command = parts[0];
        String codigo = parts[1];

        switch (command) {
            case "CONSULTAR_SALON":
                return repository.consultarSalon(codigo);
            case "RESERVAR_SALON":
                return repository.reservarSalon(codigo);
            case "LIBERAR_SALON":
                return repository.liberarSalon(codigo);
            default:
                return "ERROR_OPERACION_INVALIDA";
        }
    }
}
