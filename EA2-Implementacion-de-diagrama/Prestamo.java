import java.util.Date;
import java.util.List;
import java.util.ArrayList;

public class Prestamo {
    private String idPrestamo;
    private Date fechaPrestamo;
    private Date fechaDevolucionEsperada;
    private Date fechaDevolucionReal;
    private double multa;
    private String estado;
    
    // Relación de agregación: Un préstamo "incluye" de 1 a muchos Libros
    private List<Libro> librosPrestados;

    public Prestamo(String idPrestamo, Date fechaPrestamo, Date fechaDevolucionEsperada) {
        this.idPrestamo = idPrestamo;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
        this.librosPrestados = new ArrayList<>(); // Inicializar la lista
    }

    public double calcularMulta() {
        // Lógica para calcular la multa
        return 0.0;
    }

    public boolean estaVencido() {
        // Lógica para comparar fechas
        return false;
    }

    public void registrarDevolucion(Date fecha) {
        this.fechaDevolucionReal = fecha;
        this.estado = "Devuelto";
    }
    
    // Método para agregar libros al préstamo
    public void agregarLibro(Libro libro) {
        this.librosPrestados.add(libro);
    }
}