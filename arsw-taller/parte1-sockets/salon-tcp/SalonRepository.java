import java.util.HashMap;
import java.util.Map;

public class SalonRepository {
    private Map<String, Salon> salones = new HashMap<>();

    public SalonRepository() {
        salones.put("E301", new Salon("E301"));
        salones.put("E302", new Salon("E302"));
        salones.put("E303", new Salon("E303"));
        salones.put("E304", new Salon("E304"));
    }

    public Salon findByCodigo(String codigo) {
        return salones.get(codigo);
    }
    
    public synchronized String consultarSalon(String codigo) {
        Salon salon = findByCodigo(codigo);
        if (salon == null) return "ERROR_SALON_NO_EXISTE";
        return salon.getEstado().equals("disponible") ? "SALON_DISPONIBLE" : "SALON_RESERVADO";
    }

    public synchronized String reservarSalon(String codigo) {
        Salon salon = findByCodigo(codigo);
        if (salon == null) return "ERROR_SALON_NO_EXISTE";
        if (salon.getEstado().equals("reservado")) return "ERROR_OPERACION_INVALIDA";
        salon.setEstado("reservado");
        return "RESERVA_EXITOSA";
    }

    public synchronized String liberarSalon(String codigo) {
        Salon salon = findByCodigo(codigo);
        if (salon == null) return "ERROR_SALON_NO_EXISTE";
        salon.setEstado("disponible");
        return "LIBERACION_EXITOSA";
    }
}
