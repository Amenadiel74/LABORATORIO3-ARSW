import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.Scanner;

public class EquipoRmiClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 23001);
        EquipoService service = (EquipoService) registry.lookup("equipoService");
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== Cliente Equipo RMI ===");
        System.out.println("1. Consultar todos los equipos");
        System.out.println("2. Consultar equipo por código");
        System.out.println("3. Reservar equipo");
        System.out.println("4. Liberar equipo");
        System.out.println("5. Salir");
        
        while (true) {
            System.out.print("\nSeleccione opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine();
            
            if (opcion == 5) break;
            
            String codigo;
            switch (opcion) {
                case 1:
                    List<String> equipos = service.consultarEquipos();
                    for (String e : equipos) System.out.println(e);
                    break;
                case 2:
                    System.out.print("Código: ");
                    codigo = scanner.nextLine();
                    System.out.println(service.consultarEquipo(codigo));
                    break;
                case 3:
                    System.out.print("Código a reservar: ");
                    codigo = scanner.nextLine();
                    System.out.println("Reserva exitosa: " + service.reservarEquipo(codigo));
                    break;
                case 4:
                    System.out.print("Código a liberar: ");
                    codigo = scanner.nextLine();
                    System.out.println("Liberación exitosa: " + service.liberarEquipo(codigo));
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        }
        scanner.close();
    }
}
