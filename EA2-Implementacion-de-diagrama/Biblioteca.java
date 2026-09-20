import java.util.ArrayList;
import java.util.List;


public class Biblioteca {
    private String nombre;
    
    private final List<Libro> catalogo;
    private final List<Prestamo> prestamos;

    public Biblioteca(String nombre) {
        this.nombre = nombre;
        this.catalogo = new ArrayList<>();
        this.prestamos = new ArrayList<>();
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void ingresarLibro(String numeroSerie, int cantidad, boolean disponibilidad) {
  
    }

    public void ingresarLibro(Libro nuevoLibro) {
        this.catalogo.add(nuevoLibro);
    }

    public Libro buscarLibroPorSerie(String numeroSerie) {
        for (Libro libro : catalogo) {
            if (libro.getNumeroSerie().equals(numeroSerie)) {
                return libro; 
            }
        }
        return null; 
    }

    public void registrarPrestamo(Prestamo prestamo) {
        this.prestamos.add(prestamo);
    }
}