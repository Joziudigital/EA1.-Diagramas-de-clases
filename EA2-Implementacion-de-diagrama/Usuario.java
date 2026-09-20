import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class Usuario {
    public static final int MAX_PRESTAMOS_ACTIVOS = 3;

    // Encapsulamiento
    private String idUsuario;
    private String nombre;
    private String correo;
    private List<Prestamo> prestamos;

    public Usuario(String idUsuario, String nombre, String correo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.correo = correo;
        this.prestamos = new ArrayList<>();
    }



    public void agregarPrestamo(Prestamo prestamo) {
        if (prestamo != null) {
            this.prestamos.add(prestamo);
        }
    }

    public List<Prestamo> getPrestamosActivos() {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.estaActivo()) {
                activos.add(p);
            }
        }
        return activos;
    }

    public boolean puedePedirPrestado() {
        List<Prestamo> activos = getPrestamosActivos();
        if (activos.size() >= MAX_PRESTAMOS_ACTIVOS) {
            return false;
        }
        for (Prestamo p : activos) {
            if (p.estaVencido()) {
                return false;
            }
        }
        return true;
    }

    public double calcularTotalMultas() {
        double total = 0;
        for (Prestamo p : prestamos) {
            total += p.getMulta();
        }
        return total;
    }

    @Override
    public String toString() {
        return idUsuario + " - " + nombre + " (" + correo + ")";
    }


    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

 
    public List<Prestamo> getPrestamos() {
        return Collections.unmodifiableList(prestamos);
    }
}