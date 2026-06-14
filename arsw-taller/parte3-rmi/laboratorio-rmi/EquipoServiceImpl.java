import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipoServiceImpl extends UnicastRemoteObject implements EquipoService {
    private Map<String, Equipo> equipos = new HashMap<>();

    public EquipoServiceImpl() throws RemoteException {
        equipos.put("PC-101", new Equipo("PC-101", "Desktop Dell", "Lab-Redes"));
        equipos.put("PC-102", new Equipo("PC-102", "Desktop Dell", "Lab-Redes"));
        equipos.put("LAP-01", new Equipo("LAP-01", "Laptop HP", "Lab-Software"));
        equipos.put("LAP-02", new Equipo("LAP-02", "Laptop HP", "Lab-Software"));
        equipos.put("LAP-03", new Equipo("LAP-03", "Laptop Mac", "Lab-Software"));
    }

    @Override
    public List<String> consultarEquipos() throws RemoteException {
        List<String> list = new ArrayList<>();
        for (Equipo e : equipos.values()) {
            list.add(e.toString());
        }
        return list;
    }

    @Override
    public String consultarEquipo(String codigo) throws RemoteException {
        Equipo e = equipos.get(codigo);
        if (e == null) return "Equipo no encontrado";
        return e.toString();
    }

    @Override
    public synchronized boolean reservarEquipo(String codigo) throws RemoteException {
        Equipo e = equipos.get(codigo);
        if (e != null && e.getEstado().equals("disponible")) {
            e.setEstado("reservado");
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean liberarEquipo(String codigo) throws RemoteException {
        Equipo e = equipos.get(codigo);
        if (e != null && e.getEstado().equals("reservado")) {
            e.setEstado("disponible");
            return true;
        }
        return false;
    }
}
