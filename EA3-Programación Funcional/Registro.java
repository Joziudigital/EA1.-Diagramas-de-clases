import java.time.LocalDateTime;

// Al usar 'record', Java hace que todos estos campos sean inmutables (finales) automáticamente.
public record Registro(
    String idUsuario,
    String ruta,
    String estacion,
    String accion, // "entrada" o "salida"
    LocalDateTime timestamp
) {}