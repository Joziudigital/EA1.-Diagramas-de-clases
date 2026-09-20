import java.util.Date;


public class LibroFisico extends Libro {
    
    private String ubicacion;          // Ej: "Estante A-3"
    private int numeroPaginas;
    private String estadoConservacion; // "Bueno", "Regular" o "Deteriorado"

    public LibroFisico(String numeroSerie, String titulo, int cantidad, Date fechaPublicacion,
                       Autor autor, String ubicacion, int numeroPaginas) {
        super(numeroSerie, titulo, cantidad, fechaPublicacion, autor); // Herencia
        this.ubicacion = ubicacion;
        setNumeroPaginas(numeroPaginas);
        this.estadoConservacion = "Bueno";
    }



    @Override
    public boolean estaDisponible() {
        return getCantidad() > 0;
    }

    @Override
    public boolean prestar() {
        if (!estaDisponible()) {
            return false;
        }
        setCantidad(getCantidad() - 1); // sale un ejemplar del estante
        return true;
    }

    @Override
    public void devolver() {
        setCantidad(getCantidad() + 1); // el ejemplar vuelve al estante
    }

    @Override
    public String toString() {
        return "[Físico] " + getNumeroSerie() + " - " + getTitulo()
                + " | ejemplares en estante: " + getCantidad()
                + " | ubicación: " + ubicacion
                + " | estado: " + estadoConservacion;
    }


    public void devolver(String nuevoEstadoConservacion) {
        setEstadoConservacion(nuevoEstadoConservacion);
        devolver();
    }

    

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public int getNumeroPaginas() {
        return numeroPaginas;
    }

    public void setNumeroPaginas(int numeroPaginas) {
        if (numeroPaginas <= 0) {
            throw new IllegalArgumentException("El número de páginas debe ser mayor que 0");
        }
        this.numeroPaginas = numeroPaginas;
    }

    public String getEstadoConservacion() {
        return estadoConservacion;
    }

    public void setEstadoConservacion(String estadoConservacion) {
        this.estadoConservacion = estadoConservacion;
    }
}