import java.util.Date;


public class LibroFisico extends Libro {

    private String ubicacionEstante;
    private int copiasDisponibles;

    public LibroFisico(String numeroSerie, String titulo, int cantidad,
                        Date fechaPublicacion, Autor autor, String ubicacionEstante) {
        super(numeroSerie, titulo, cantidad, fechaPublicacion, autor);
        this.ubicacionEstante = ubicacionEstante;
        // Al ingresar el libro, todas las copias empiezan disponibles
        this.copiasDisponibles = cantidad;
    }


    @Override
    public boolean estaDisponible() {
        return copiasDisponibles > 0;
    }


    public boolean prestar() {
        return prestar(1);
    }

    public boolean prestar(int cantidadSolicitada) {
        if (cantidadSolicitada <= 0 || copiasDisponibles < cantidadSolicitada) {
            return false;
        }
        copiasDisponibles -= cantidadSolicitada;
        return true;
    }


    public void devolver() {
        devolver(1);
    }

    public void devolver(int cantidadDevuelta) {
        if (cantidadDevuelta <= 0) return;
        copiasDisponibles = Math.min(getCantidad(), copiasDisponibles + cantidadDevuelta);
    }

    public String getUbicacionEstante() {
        return ubicacionEstante;
    }

    public void setUbicacionEstante(String ubicacionEstante) {
        this.ubicacionEstante = ubicacionEstante;
    }

    public int getCopiasDisponibles() {
        return copiasDisponibles;
    }

    @Override
    public String toString() {
        return "LibroFisico{" + getTitulo() + ", copias disponibles=" + copiasDisponibles +
                ", estante=" + ubicacionEstante + "}";
    }
}