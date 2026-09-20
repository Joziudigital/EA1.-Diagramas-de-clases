import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("=== SISTEMA DE GESTIÓN DE BIBLIOTECA ===\n");

        //1. Crear la Biblioteca
        System.out.print("Nombre de la biblioteca: ");
        String nombreBiblioteca = leerTexto(sc, "Biblioteca Central");
        Biblioteca biblioteca = new Biblioteca(nombreBiblioteca);

        //2. Registrar Autor
        System.out.println("\n-- Registro de Autor --");
        System.out.print("Nombre del autor: ");
        String nombreAutor = leerTexto(sc, "Autor Desconocido");
        System.out.print("Nacionalidad: ");
        String nacionalidad = leerTexto(sc, "N/D");
        Autor autor = new Autor(nombreAutor, new Date(), nacionalidad);

        //3. Registrar Libro Físico
        System.out.println("\n-- Registro de Libro Físico --");
        System.out.print("Número de serie: ");
        String serieFisico = leerTexto(sc, "F-001");
        System.out.print("Título: ");
        String tituloFisico = leerTexto(sc, "Sin título");
        System.out.print("Cantidad de copias: ");
        int cantidadFisico = leerEntero(sc, 1);
        System.out.print("Ubicación en el estante: ");
        String ubicacion = leerTexto(sc, "Estante A1");

        LibroFisico libroFisico = new LibroFisico(
                serieFisico, tituloFisico, cantidadFisico, new Date(), autor, ubicacion);
        biblioteca.ingresarLibro(libroFisico); // usa la sobrecarga que recibe un Libro

        //4. Registrar Libro Digital
        System.out.println("\n-- Registro de Libro Digital --");
        System.out.print("Número de serie: ");
        String serieDigital = leerTexto(sc, "D-001");
        System.out.print("Título: ");
        String tituloDigital = leerTexto(sc, "Sin título");
        System.out.print("Formato (PDF/EPUB): ");
        String formato = leerTexto(sc, "PDF");
        System.out.print("Licencias simultáneas disponibles: ");
        int licencias = leerEntero(sc, 1);

        LibroDigital libroDigital = new LibroDigital(
                serieDigital, tituloDigital, licencias, new Date(), autor, formato, 4.5, licencias);
        biblioteca.ingresarLibro(libroDigital);

        //5. POLIMORFISMO
        // Una misma lista de tipo Libro contiene tanto LibroFisico como
        // LibroDigital; al llamar estaDisponible() se ejecuta la versión
        // sobrescrita de cada subclase automáticamente.
        List<Libro> catalogoDemo = Arrays.asList(libroFisico, libroDigital);
        System.out.println("\n-- Catálogo registrado (demostración de polimorfismo) --");
        for (Libro libro : catalogoDemo) {
            System.out.println(" - " + libro.getTitulo() + " | disponible: " + libro.estaDisponible());
        }

        //6. Registrar Usuario
        System.out.println("\n-- Registro de Usuario --");
        System.out.print("ID de usuario: ");
        String idUsuario = leerTexto(sc, "U-001");
        System.out.print("Nombre: ");
        String nombreUsuario = leerTexto(sc, "Usuario Anónimo");
        System.out.print("Email: ");
        String email = leerTexto(sc, "correo@ejemplo.com");
        Usuario usuario = new Usuario(idUsuario, nombreUsuario, email);

        //7. Registrar Préstamo
        System.out.println("\n-- Registro de Préstamo --");
        System.out.print("¿Qué libro desea prestar? (1 = Físico, 2 = Digital): ");
        int opcion = leerEntero(sc, 1);
        Libro libroElegido = (opcion == 2) ? libroDigital : libroFisico;

        Date hoy = new Date();
        long MILLIS_POR_DIA = 24L * 60 * 60 * 1000;
        Date devolucionEsperada = new Date(hoy.getTime() + 7 * MILLIS_POR_DIA);

        // Composición
        Prestamo prestamo = new Prestamo("P-" + idUsuario, hoy, devolucionEsperada);
        // Agregación
        prestamo.agregarLibro(libroElegido);
        prestamo.setEstado("Activo");

        boolean exito;
        if (libroElegido instanceof LibroFisico) {
            exito = ((LibroFisico) libroElegido).prestar();
        } else {
            exito = ((LibroDigital) libroElegido).descargar();
        }

        if (exito) {
            biblioteca.registrarPrestamo(prestamo);
            usuario.solicitarPrestamo(prestamo);
            System.out.println("\n✔ Préstamo registrado con éxito para \"" + libroElegido.getTitulo() + "\".");
        } else {
            System.out.println("\n✘ No hay disponibilidad para prestar ese libro en este momento.");
        }

        // 8. Búsqueda en la biblioteca
        System.out.println("\n-- Búsqueda por número de serie --");
        Libro encontrado = biblioteca.buscarLibroPorSerie(libroElegido.getNumeroSerie());
        System.out.println("Resultado: " + (encontrado != null ? encontrado.getTitulo() : "No encontrado"));

        // Resumen final 
        System.out.println("\n=== RESUMEN ===");
        System.out.println("Biblioteca: " + biblioteca.getNombre());
        System.out.println("Autor: " + autor.getNombre() + " (" + autor.getNacionalidad() + ")");
        System.out.println("Usuario: " + usuario.getNombre() + " | préstamos activos: " + usuario.cantidadPrestamosActivos());

        sc.close();
    }


    private static String leerTexto(Scanner sc, String valorPorDefecto) {
        String entrada = sc.nextLine();
        return (entrada == null || entrada.trim().isEmpty()) ? valorPorDefecto : entrada.trim();
    }

    private static int leerEntero(Scanner sc, int valorPorDefecto) {
        String entrada = sc.nextLine();
        try {
            return Integer.parseInt(entrada.trim());
        } catch (NumberFormatException e) {
            return valorPorDefecto;
        }
    }
}