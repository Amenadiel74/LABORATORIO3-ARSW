import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SalonClient {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Comandos: CONSULTAR_SALON,E303 | RESERVAR_SALON,E303 | LIBERAR_SALON,E303");
        while (true) {
            System.out.print("Ingrese comando: ");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("exit")) break;

            try {
                Socket socket = new Socket("127.0.0.1", 35001);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

                out.println(input);
                String response = in.readLine();
                System.out.println("Respuesta del servidor: " + response);

                in.close();
                out.close();
                socket.close();
            } catch (Exception e) {
                System.out.println("Error de conexión: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
