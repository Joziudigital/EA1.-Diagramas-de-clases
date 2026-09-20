
import java.util.Date;


public class LibroDigital extends Libro {
    private String formato;      // Puede ser del tipo .pdf o publicación electrónica .epub
    private double tamanoMB;
    private String urlDescarga;
    private int licenciasEnUso;  

    public LibroDigital(String numeroSerie, String titulo, int cantidad, Date fechaPublicacion,
                        Autor autor, String formato, double tamanoMB, String urlDescarga) {
        super(numeroSerie, titulo, cantidad, fechaPublicacion, autor); // Herencia
        this.formato = formato;
        setTamanoMB(tamanoMB);
        this.urlDescarga = urlDescarga;
        this.licenciasEnUso = 0;
    }



    @Override
    public boolean estaDisponible() {
        return licenciasEnUso < getCantidad();
    }

    @Override
    public boolean prestar() {
        if (!estaDisponible()) {
            return false;
        }
        licenciasEnUso++;
        return true;
    }

    @Override
    public void devolver() {
        if (licenciasEnUso > 0) {
            licenciasEnUso--;
        }
    }

    @Override
    public String toString() {
        return "[Digital] " + getNumeroSerie() + " - " + getTitulo()
                + " | licencias libres: " + getLicenciasDisponibles() + "/" + getCantidad()
                + " | formato: " + formato + " (" + tamanoMB + " MB)";
    }



    public int getLicenciasDisponibles() {
        return getCantidad() - licenciasEnUso;
    }



    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public double getTamanoMB() {
        return tamanoMB;
    }

    public void setTamanoMB(double tamanoMB) {
        if (tamanoMB <= 0) {
            throw new IllegalArgumentException("El tamaño debe ser mayor que 0");
        }
        this.tamanoMB = tamanoMB;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }

 
    public int getLicenciasEnUso() {
        return licenciasEnUso;
    }
}