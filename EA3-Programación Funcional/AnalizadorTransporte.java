import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AnalizadorTransporte {

  // a) Cálculo de afluencia por estación
  public Map<String, Long> calcularAfluenciaPorEstacion(List<Registro> registros) {
    return registros.stream()
        .filter(r -> r.accion().equalsIgnoreCase("entrada"))
        .collect(Collectors.groupingBy(Registro::estacion, Collectors.counting()));
  }

    // b) Identificación de horas pico
    public Map<Integer, Long> identificarHorasPico(List<Registro> registros) {
      return registros.stream()
          .collect(Collectors.groupingBy(r -> r.timestamp().getHour(), Collectors.counting()));
    }

    // c) Rutas más utilizadas (Ordenadas de mayor a menor uso)
    public List<Map.Entry<String, Long>> rutasMasUtilizadas(List<Registro> registros) {
        return registros.stream()
                .collect(Collectors.groupingBy(Registro::ruta, Collectors.counting()))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .collect(Collectors.toList());
    }

}