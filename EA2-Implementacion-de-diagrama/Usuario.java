import java.util.ArrayList;
import java.util.List;


public class Usuario {

    private String id;
    private String nombre;
    private String email;
    private final List<Prestamo> historialPrestamos;

    public Usuario(String id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.historialPrestamos = new ArrayList<>();
    }

    public void solicitarPrestamo(Prestamo prestamo) {
        this.historialPrestamos.add(prestamo);
    }

    public int cantidadPrestamosActivos() {
        int contador = 0;
        for (Prestamo p : historialPrestamos) {
            if (!"Devuelto".equals(p.getEstado())) {
                contador++;
            }
        }
        return contador;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Prestamo> getHistorialPrestamos() {
        return historialPrestamos;
    }

    @Override
    public String toString() {
        return "Usuario{" + nombre + ", id=" + id + ", email=" + email + "}";
    }
}