import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // --- GETTERS Y SETTERS ---
    public String getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(String idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public Date getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(Date fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public Date getFechaDevolucionEsperada() {
        return fechaDevolucionEsperada;
    }

    public void setFechaDevolucionEsperada(Date fechaDevolucionEsperada) {
        this.fechaDevolucionEsperada = fechaDevolucionEsperada;
    }

    public Date getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(Date fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
    }

    public double getMulta() {
        return multa;
    }

    public void setMulta(double multa) {
        this.multa = multa;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }