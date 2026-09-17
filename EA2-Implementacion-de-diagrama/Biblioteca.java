import java.util.List;
import java.util.ArrayList;

public class Biblioteca {
    private String nombre;
    
    // Relaciones de composición
    private List<Libro> catalogo;
    private List<Prestamo> prestamos;

    public Biblioteca(String nombre) {
        this.nombre = nombre;
        this.catalogo = new ArrayList<>();
        this.prestamos = new ArrayList<>();
    }

    // 1. Método original que está en tu diagrama
    public void ingresarLibro(String numeroSerie, int cantidad, boolean disponibilidad) {
        // Lógica para buscar el libro y actualizar su stock
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