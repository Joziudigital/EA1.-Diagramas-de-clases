import java.util.ArrayList;
import java.util.List;


public class Biblioteca {
    private String nombre;
    
    private List<Libro> catalogo;
    private List<Prestamo> prestamos;

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

    // 2. SOBRECARGA DEL MÉTODO
    public void ingresarLibro(Libro nuevoLibro) {
        this.catalogo.add(nuevoLibro);
    }

    public Libro buscarLibroPorSerie(String numeroSerie) {
        for (Libro libro : catalogo) {
            if (libro.getNumeroSerie().equals(numeroSerie)) {
                return libro; // Polimorfismo: Retorna Libro, no importa si es físico o digital
            }
        }
        return null; 
    }

    public void registrarPrestamo(Prestamo prestamo) {
        this.prestamos.add(prestamo);
    }
}