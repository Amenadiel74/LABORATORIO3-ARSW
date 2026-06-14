import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class EquipoRmiServer {
    public static void main(String[] args) throws Exception {
        EquipoService service = new EquipoServiceImpl();
        Registry registry = LocateRegistry.createRegistry(23001);
        registry.rebind("equipoService", service);
        System.out.println("EquipoService RMI publicado en puerto 23001...");
    }
}
