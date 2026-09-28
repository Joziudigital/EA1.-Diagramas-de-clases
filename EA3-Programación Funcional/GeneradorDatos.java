import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

// se crea para mantener el código de generación de datos aleatorios separado del resto del programa
public class GeneradorDatos {

    private static final List<String> ESTACIONES = List.of(
        "Estacion Norte", "Estacion Centro", "Estacion Sur",
        "Estacion Este", "Estacion Oeste", "Estacion Universidad");

    private static final List<String> RUTAS = List.of(
        "Ruta A", "Ruta B", "Ruta C", "Ruta D", "Ruta E");

    // Las horas repetidas pesan más al sortear para crear horas pico realistas
    private static final List<Integer> HORAS_IDA      = List.of(6, 6, 7, 7, 7, 7, 8, 8, 9);
    private static final List<Integer> HORAS_REGRESO  = List.of(12, 13, 17, 17, 18, 18, 18, 19, 21);

    public static List<Registro> generar(int cantidadUsuarios) {
        LocalDate dia = LocalDate.of(2026, 9, 27);
        return IntStream.rangeClosed(1, cantidadUsuarios)
            .boxed()
            .flatMap(numero -> viajesDeUsuario(numero, dia))
            .toList(); // lista inmutable
    }

    private static Stream<Registro> viajesDeUsuario(int numero, LocalDate dia) {
        Random azar = new Random(numero);
        String idUsuario = String.format("U%06d", numero);

        int origen = azar.nextInt(ESTACIONES.size());
        // Suma entre 1 y (n-1) para garantizar que el destino siempre sea distinto al origen
        int destino = (origen + 1 + azar.nextInt(ESTACIONES.size() - 1)) % ESTACIONES.size();
        String ruta = RUTAS.get(azar.nextInt(RUTAS.size()));

        return Stream.concat(
            viaje(idUsuario, ruta, ESTACIONES.get(origen), ESTACIONES.get(destino), HORAS_IDA, dia, azar),
            viaje(idUsuario, ruta, ESTACIONES.get(destino), ESTACIONES.get(origen), HORAS_REGRESO, dia, azar));
    }

    private static Stream<Registro> viaje(String idUsuario, String ruta, String estacionOrigen,
                                          String estacionDestino, List<Integer> horas,
                                          LocalDate dia, Random azar) {
        LocalDateTime entrada = dia.atTime(horas.get(azar.nextInt(horas.size())), azar.nextInt(60));
        LocalDateTime salida = entrada.plusMinutes(10 + azar.nextInt(50)); // viajes de 10 a 59 min

        return Stream.of(
            new Registro(idUsuario, ruta, estacionOrigen, "entrada", entrada),
            new Registro(idUsuario, ruta, estacionDestino, "salida", salida));
    }
}