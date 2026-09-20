
import java.util.Date;


public class LibroDigital extends Libro {

    private String formato;              // PDF, EPUB, MOBI, etc.
    private double tamanoMB;
    private int licenciasSimultaneas;    // cuántas personas pueden tenerlo abierto a la vez
    private int licenciasEnUso;

    public LibroDigital(String numeroSerie, String titulo, int cantidad, Date fechaPublicacion,
                         Autor autor, String formato, double tamanoMB, int licenciasSimultaneas) {
        super(numeroSerie, titulo, cantidad, fechaPublicacion, autor);
        this.formato = formato;
        this.tamanoMB = tamanoMB;
        this.licenciasSimultaneas = licenciasSimultaneas;
        this.licenciasEnUso = 0;
    }


    @Override
    public boolean estaDisponible() {
        return licenciasEnUso < licenciasSimultaneas;
    }

    public boolean descargar() {
        return descargar(this.formato);
    }

    public boolean descargar(String formatoSolicitado) {
        if (!estaDisponible()) {
            return false;
        }
        licenciasEnUso++;
        System.out.println("Descargando \"" + getTitulo() + "\" en formato " + formatoSolicitado + "...");
        return true;
    }

    public void liberarLicencia() {
        if (licenciasEnUso > 0) {
            licenciasEnUso--;
        }
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
        this.tamanoMB = tamanoMB;
    }

    public int getLicenciasSimultaneas() {
        return licenciasSimultaneas;
    }

    public void setLicenciasSimultaneas(int licenciasSimultaneas) {
        this.licenciasSimultaneas = licenciasSimultaneas;
    }

    public int getLicenciasEnUso() {
        return licenciasEnUso;
    }

    @Override
    public String toString() {
        return "LibroDigital{" + getTitulo() + ", formato=" + formato +
                ", licencias=" + licenciasEnUso + "/" + licenciasSimultaneas + "}";
    }
}