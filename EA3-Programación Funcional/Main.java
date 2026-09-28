import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    // Estado de la interfaz (los datos y el modo que el usuario elige en el menú)
    private static List<Registro> registros = datosDePrueba();
    private static AnalizadorTransporte analizador = new AnalizadorTransporte();
    private static long umbralSugerido = 2;

    public static void main(String[] args) {
        try (Scanner teclado = new Scanner(System.in)) {
            ejecutarMenu(teclado);
        }
    }

    private static void ejecutarMenu(Scanner teclado) {
        boolean continuar = true;

        while (continuar) {
            mostrarMenu();
            if (!teclado.hasNextLine()) {
                break; // la entrada se cerró (Ctrl+D / Ctrl+Z): se termina sin error
            }
            String opcion = teclado.nextLine().trim();

            switch (opcion) {

                case "1" -> ejecutar("1) Afluencia por estación (entradas)", () ->
                    analizador.calcularAfluenciaPorEstacion(registros).forEach((estacion, cantidad) ->
                        System.out.println(" - " + estacion + ": " + cantidad)));

                case "2" -> ejecutar("2) Horas pico (las 3 horas con más movimientos)", () ->
                    analizador.identificarHorasPico(registros, 3).forEach(hora ->
                        System.out.printf(" - %02d:00 hrs -> %d registros%n", hora.getKey(), hora.getValue())));

                case "3" -> ejecutar("3) Rutas más utilizadas", () ->
                    analizador.rutasMasUtilizadas(registros).forEach(ruta ->
                        System.out.println(" - " + ruta.getKey() + ": " + ruta.getValue() + " movimientos")));

                case "4" -> ejecutar("4) Patrones de viaje por usuario (primeros 10)", () -> {
                    Map<String, List<String>> patrones = analizador.patronesViajePorUsuario(registros);
                    patrones.entrySet().stream().limit(10).forEach(usuario ->
                        System.out.println(" - " + usuario.getKey() + ": " + String.join(" | ", usuario.getValue())));
                    System.out.println(" (Usuarios analizados en total: " + patrones.size() + ")");
                });

                case "5" -> ejecutar("5) Tiempo promedio entre estaciones", () -> {
                    analizador.calcularTiempoPromedioEntreEstaciones(registros).forEach((tramo, minutos) ->
                        System.out.printf(" - %s: %.1f min%n", tramo, minutos));
                    System.out.printf(" >> Promedio general: %.1f min%n",
                        analizador.calcularTiempoPromedioGeneral(registros));
                });

                case "6" -> {
                    long umbral = leerUmbral(teclado);
                    ejecutar("6) Rutas críticas (más de " + umbral + " entradas)", () -> {
                        Map<String, Long> criticas = analizador.detectarSobrecargaEnRutas(registros, umbral);
                        if (criticas.isEmpty()) {
                            System.out.println(" - Ninguna ruta reporta sobrecarga crítica.");
                        } else {
                            criticas.forEach((ruta, entradas) ->
                                System.out.println(" - ¡ALERTA! " + ruta + " -> " + entradas + " entradas"));
                        }
                    });
                }

                case "7" -> {
                    registros = datosDePrueba();
                    umbralSugerido = 2;
                    System.out.println("Datos de prueba cargados: " + registros.size() + " registros.");
                }

                case "8" -> {
                    long inicio = System.currentTimeMillis();
                    registros = GeneradorDatos.generar(250_000);
                    umbralSugerido = 100_000;
                    System.out.println("Datos simulados generados: " + registros.size()
                        + " registros (" + (System.currentTimeMillis() - inicio) + " ms).");
                }

                case "9" -> {
                    analizador = new AnalizadorTransporte(!analizador.esParalelo());
                    System.out.println("Modo de procesamiento: "
                        + (analizador.esParalelo() ? "PARALELO" : "SECUENCIAL"));
                }

                case "0" -> {
                    System.out.println("Hasta luego.");
                    continuar = false;
                }

                default -> System.out.println("Opción no válida. Elija un número del menú.");
            }
        }
    }

    // Ejecuta un reporte y muestra cuánto tardó (sirve para comparar secuencial vs paralelo)
    private static void ejecutar(String titulo, Runnable reporte) {
        System.out.println("\n--- " + titulo + " ---");
        long inicio = System.nanoTime();
        reporte.run();
        long milisegundos = (System.nanoTime() - inicio) / 1_000_000;
        System.out.println("(" + (analizador.esParalelo() ? "paralelo" : "secuencial")
            + ", " + milisegundos + " ms)");
    }

    private static long leerUmbral(Scanner teclado) {
        System.out.print("Umbral de ocupación [Umbral sugerido " + umbralSugerido + "]: ");
        if (!teclado.hasNextLine()) {
            return umbralSugerido;
        }
        String texto = teclado.nextLine().trim();
        try {
            return texto.isEmpty() ? umbralSugerido : Long.parseLong(texto);
        } catch (NumberFormatException e) {
            System.out.println("Valor no válido, se usa " + umbralSugerido + ".");
            return umbralSugerido;
        }
    }

    private static void mostrarMenu() {
        System.out.println("\n----- TECNOMOVIL DATA -----");
        System.out.println("Datos: " + registros.size() + " registros | Modo: "
            + (analizador.esParalelo() ? "PARALELO" : "SECUENCIAL"));
        System.out.println("1. Afluencia por estación");
        System.out.println("2. Horas pico");
        System.out.println("3. Rutas más utilizadas");
        System.out.println("4. Patrones de viaje por usuario");
        System.out.println("5. Tiempo promedio entre estaciones");
        System.out.println("6. Detección de sobrecarga en rutas");
        System.out.println("7. Usar datos de prueba (6 registros)");
        System.out.println("8. Generar datos simulados grandes (1.000.000 registros)");
        System.out.println("9. Cambiar modo secuencial / paralelo");
        System.out.println("0. Salir");
        System.out.print("Opción: ");
    }

    // Datos simulados (quemados) para prueba
    private static List<Registro> datosDePrueba() {
        return List.of(
            new Registro("U001", "Ruta A", "Estacion Norte", "entrada", LocalDateTime.of(2026, 9, 27, 7, 0)),
            new Registro("U001", "Ruta A", "Estacion Centro", "salida", LocalDateTime.of(2026, 9, 27, 7, 30)), // 30 mins
            new Registro("U002", "Ruta B", "Estacion Sur", "entrada", LocalDateTime.of(2026, 9, 27, 8, 15)),
            new Registro("U002", "Ruta B", "Estacion Centro", "salida", LocalDateTime.of(2026, 9, 27, 8, 45)), // 30 mins
            new Registro("U003", "Ruta A", "Estacion Norte", "entrada", LocalDateTime.of(2026, 9, 27, 7, 10)),
            new Registro("U004", "Ruta A", "Estacion Norte", "entrada", LocalDateTime.of(2026, 9, 27, 7, 15))
        );
    }
}