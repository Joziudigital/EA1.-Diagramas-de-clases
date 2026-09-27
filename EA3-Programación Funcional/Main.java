import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Datos simulados (quemados) para prueba
        List<Registro> registros = List.of(
            new Registro("U001", "Ruta A", "Estacion Norte", "entrada", LocalDateTime.of(2026, 9, 27, 7, 0)),
            new Registro("U001", "Ruta A", "Estacion Centro", "salida", LocalDateTime.of(2026, 9, 27, 7, 30)), // 30 mins
            new Registro("U002", "Ruta B", "Estacion Sur", "entrada", LocalDateTime.of(2026, 9, 27, 8, 15)),
            new Registro("U002", "Ruta B", "Estacion Centro", "salida", LocalDateTime.of(2026, 9, 27, 8, 45)), // 30 mins
            new Registro("U003", "Ruta A", "Estacion Norte", "entrada", LocalDateTime.of(2026, 9, 27, 7, 10)),
            new Registro("U004", "Ruta A", "Estacion Norte", "entrada", LocalDateTime.of(2026, 9, 27, 7, 15))
        );

        AnalizadorTransporte analizador = new AnalizadorTransporte();

        System.out.println("--- REPORTES TECNOMOVIL DATA ---");

        System.out.println("\na) Afluencia por estación (entradas):");
        analizador.calcularAfluenciaPorEstacion(registros).forEach((estacion, cantidad) -> 
            System.out.println(" - " + estacion + ": " + cantidad));

        System.out.println("\nb) Horas pico (movimientos globales):");
        analizador.identificarHorasPico(registros).forEach((hora, cantidad) -> 
            System.out.println(" - " + hora + ":00 Hrs -> " + cantidad + " registros"));

        System.out.println("\nc) Rutas más utilizadas:");
        analizador.rutasMasUtilizadas(registros).forEach(entry -> 
            System.out.println(" - " + entry.getKey() + ": " + entry.getValue() + " movimientos"));

        System.out.println("\nd) Patrones de viaje por usuario:");
        analizador.patronesViajePorUsuario(registros).forEach((usuario, estaciones) -> 
            System.out.println(" - " + usuario + " visitó: " + estaciones));

        System.out.println("\ne) Tiempo promedio de viaje (General):");
        System.out.println(" - " + analizador.calcularTiempoPromedioEntreEstaciones(registros) + " minutos");

        System.out.println("\nf) Rutas críticas (sobrecarga > 2 entradas):");
        List<String> rutasCriticas = analizador.detectarSobrecargaEnRutas(registros, 2);
        if (rutasCriticas.isEmpty()) {
            System.out.println(" - Ninguna ruta reporta sobrecarga crítica.");
        } else {
            rutasCriticas.forEach(ruta -> System.out.println(" - ¡ALERTA! " + ruta));
        }
    }
}