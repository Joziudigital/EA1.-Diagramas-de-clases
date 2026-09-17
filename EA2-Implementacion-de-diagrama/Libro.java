import java.util.Date;

public abstract class Libro {
  private String numeroSerie;
  private String titulo;
  private int cantidad;
  private Date fechaPublicacion;
  private Autor autor;

  public Libro(String numeroSerie, String titulo, int cantidad, Date fechaPublicacion, Autor autor) {
    this.numeroSerie = numeroSerie;
    this.titulo = titulo;
    this.cantidad = cantidad;
    this.fechaPublicacion = fechaPublicacion;
    this.autor = autor;
  }

  // Método abstracto (Polimorfismo)
  public abstract boolean estaDisponible();

  public String getNumeroSerie() {
    return numeroSerie;
  }

  public String getTitulo() {
    return titulo;
  }

  public int getCantidad() {
    return cantidad;
  }

  public Date getFechaPublicacion() {
    return fechaPublicacion;
  }

  public Autor getAutor() {
    return autor;
  }

  public void setNumeroSerie(String numeroSerie) {
    this.numeroSerie = numeroSerie;
  }

  public void setCantidad(int cantidad) {
    this.cantidad = cantidad;
  }

  public void setAutor(Autor autor) {
    this.autor = autor;
  }

  public void setTitulo(String titulo) {
    this.titulo = titulo;
  }

  public void setFechaPublicacion(Date fechaPublicacion) {
    this.fechaPublicacion = fechaPublicacion;
  }
}