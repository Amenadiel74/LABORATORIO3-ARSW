import java.io.Serializable;

public class Equipo implements Serializable {
    private String codigo;
    private String nombre;
    private String laboratorio;
    private String estado;

    public Equipo(String codigo, String nombre, String laboratorio) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.laboratorio = laboratorio;
        this.estado = "disponible";
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getLaboratorio() { return laboratorio; }
    public String getEstado() { return estado; }

    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return codigo + " - " + nombre + " (" + laboratorio + ") -> " + estado;
    }
}
