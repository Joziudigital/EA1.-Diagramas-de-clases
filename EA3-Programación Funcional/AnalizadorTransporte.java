import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


public class AnalizadorTransporte {

    private final boolean paralelo; // final - el analizador también es inmutable

    public AnalizadorTransporte() {
        this(false);
    }

    public AnalizadorTransporte(boolean paralelo) {
        this.paralelo = paralelo;
    }

    public boolean esParalelo() {
        return paralelo;
    }

    // Único punto donde se decide entre stream() y parallelStream()
    private <T> Stream<T> flujo(Collection<T> datos) {
        return paralelo ? datos.parallelStream() : datos.stream();
    }

    // a) Afluencia por estación: cuántos usuarios entran a cada estación

    public Map<String, Long> calcularAfluenciaPorEstacion(List<Registro> registros) {
        return flujo(registros)
            .filter(r -> r.accion().equalsIgnoreCase("entrada"))
            .collect(Collectors.collectingAndThen(
                Collectors.groupingBy(Registro::estacion, TreeMap::new, Collectors.counting()),
                Collections::unmodifiableMap));
    }


    // b) Horas pico: agrupa por hora y devuelve las horas con más flujo

    public List<Map.Entry<Integer, Long>> identificarHorasPico(List<Registro> registros, int top) {
        return flujo(registros)
            .collect(Collectors.groupingBy(r -> r.timestamp().getHour(), Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<Integer, Long>comparingByValue().reversed()
                    .thenComparing(Map.Entry.comparingByKey())) // desempate: la hora más temprana
            .limit(top)
            .toList();
    }

    // c) Rutas más utilizadas 

    public List<Map.Entry<String, Long>> rutasMasUtilizadas(List<Registro> registros) {
        return flujo(registros)
            .collect(Collectors.groupingBy(Registro::ruta, Collectors.counting()))
            .entrySet().stream()
            .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                    .thenComparing(Map.Entry.comparingByKey()))
            .toList();
    }

    // d) Patrones de viaje: por usuario, estaciones en el orden en que aparecen

    public Map<String, List<String>> patronesViajePorUsuario(List<Registro> registros) {
        return flujo(registros)
            .collect(Collectors.collectingAndThen(
                Collectors.groupingBy(
                    Registro::idUsuario,
                    TreeMap::new,
                    Collectors.mapping(Registro::estacion, Collectors.toUnmodifiableList())),
                Collections::unmodifiableMap));
    }

    // e) Tiempo promedio (en minutos) entre estaciones consecutivas
    public Map<String, Double> calcularTiempoPromedioEntreEstaciones(List<Registro> registros) {
        return tramosDeViaje(registros)
            .collect(Collectors.collectingAndThen(
                Collectors.groupingBy(
                    Map.Entry<String, Long>::getKey,
                    TreeMap::new,
                    Collectors.averagingLong(Map.Entry<String, Long>::getValue)),
                Collections::unmodifiableMap));
    }

    // Promedio general de todos los tramos juntos
    public double calcularTiempoPromedioGeneral(List<Registro> registros) {
        return tramosDeViaje(registros)
            .mapToLong(Map.Entry::getValue)
            .average()
            .orElse(0.0);
    }

    // Convierte los registros en pares (tramo, minutos), uno por cada viaje entrada -> salida
    private Stream<Map.Entry<String, Long>> tramosDeViaje(List<Registro> registros) {
        Map<String, List<Registro>> porUsuario = flujo(registros)
            .collect(Collectors.groupingBy(Registro::idUsuario));

        return flujo(porUsuario.values())
            .flatMap(this::tramosDeUnUsuario);
    }

    private Stream<Map.Entry<String, Long>> tramosDeUnUsuario(List<Registro> delUsuario) {
        // sorted() crea una lista nueva: la original no se toca
        List<Registro> ordenados = delUsuario.stream()
            .sorted(Comparator.comparing(Registro::timestamp))
            .toList();

        // Se comparan los registros vecinos: (0,1), (1,2), (2,3)...
        return IntStream.range(0, ordenados.size() - 1)
            .mapToObj(i -> Map.entry(ordenados.get(i), ordenados.get(i + 1)))
            .filter(par -> esViaje(par.getKey(), par.getValue()))
            .map(par -> Map.entry(
                par.getKey().estacion() + " -> " + par.getValue().estacion(),
                Duration.between(par.getKey().timestamp(), par.getValue().timestamp()).toMinutes()));
    }

    // Un viaje es: "entrada" seguida de "salida" en una estación diferente
    private boolean esViaje(Registro inicio, Registro fin) {
        return inicio.accion().equalsIgnoreCase("entrada")
            && fin.accion().equalsIgnoreCase("salida")
            && !inicio.estacion().equals(fin.estacion());
    }

    // f) Sobrecarga: rutas cuya ocupación (entradas) supera el umbral "críticas"

    public Map<String, Long> detectarSobrecargaEnRutas(List<Registro> registros, long umbral) {
        return flujo(registros)
            .filter(r -> r.accion().equalsIgnoreCase("entrada"))
            .collect(Collectors.groupingBy(Registro::ruta, Collectors.counting()))
            .entrySet().stream()
            .filter(entrada -> entrada.getValue() > umbral)
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, Long::sum, TreeMap::new),
                Collections::unmodifiableMap));
    }
}