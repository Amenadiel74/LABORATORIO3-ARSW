public class Salon {
    private String codigo;
    private String estado;

    public Salon(String codigo) {
        this.codigo = codigo;
        this.estado = "disponible"; // disponible o reservado
    }

    public String getCodigo() {
        return codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
