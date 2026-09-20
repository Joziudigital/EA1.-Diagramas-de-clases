import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class Main {
    private static final long MILLIS_POR_DIA = 24L * 60 * 60 * 1000;
    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    private static final Scanner entrada = new Scanner(System.in);
    private static final List<Autor> autores = new ArrayList<>();
    private static Biblioteca biblioteca;
    private static int contadorPrestamos = 0;

    public static void main(String[] args) {
        try {
            System.out.println("=== SISTEMA DE BIBLIOTECA - PANEL DEL ADMINISTRADOR ===");
            biblioteca = new Biblioteca(leerTexto("Nombre de la biblioteca"));
            System.out.println("Bienvenido a " + biblioteca.getNombre());

            boolean salir = false;
            while (!salir) {
                mostrarMenu();
                int opcion = leerEntero("Elige una opción", 0, 11);
                switch (opcion) {
                    case 1: registrarAutor(); break;
                    case 2: registrarLibroFisico(); break;
                    case 3: registrarLibroDigital(); break;
                    case 4: ingresarExistencias(); break;
                    case 5: registrarUsuario(); break;
                    case 6: realizarPrestamo(); break;
                    case 7: registrarDevolucion(); break;
                    case 8: verCatalogo(); break;
                    case 9: verUsuariosYPrestamos(); break;
                    case 10: cargarDatosEjemplo(); break;
                    case 11: demostracionAutomatica(); break;
                    case 0: salir = true; break;
                    default: break;
                }
            }
            System.out.println("Hasta luego.");
        } catch (NoSuchElementException e) {
            // Se acabó la entrada (Ctrl+D / Ctrl+Z): salir sin error
            System.out.println("\nEntrada finalizada. Hasta luego.");
        }
    }

    // =====================================================================
    //  MENÚ
    // =====================================================================

    private static void mostrarMenu() {
        System.out.println("\n---------------- MENÚ ----------------");
        System.out.println(" 1. Registrar autor");
        System.out.println(" 2. Registrar libro físico");
        System.out.println(" 3. Registrar libro digital");
        System.out.println(" 4. Ingresar más ejemplares/licencias a un libro");
        System.out.println(" 5. Registrar usuario");
        System.out.println(" 6. Realizar préstamo");
        System.out.println(" 7. Registrar devolución");
        System.out.println(" 8. Ver catálogo");
        System.out.println(" 9. Ver usuarios y préstamos");
        System.out.println("10. Cargar datos de ejemplo");
        System.out.println("11. Ejecutar demostración automática");
        System.out.println(" 0. Salir");
        System.out.println("--------------------------------------");
    }

    // =====================================================================
    //  AUTORES Y LIBROS
    // =====================================================================

    private static Autor registrarAutor() {
        titulo("Registrar autor");
        String nombre = leerTexto("Nombre");
        Date nacimiento = leerFecha("Fecha de nacimiento");
        String nacionalidad = leerTexto("Nacionalidad");

        Autor autor = new Autor(nombre, nacimiento, nacionalidad);
        autores.add(autor);
        System.out.println("Autor registrado: " + autor.getNombre());
        return autor;
    }

    private static Autor seleccionarAutor() {
        if (autores.isEmpty()) {
            System.out.println("Aún no hay autores registrados. Registra uno:");
            return registrarAutor();
        }
        System.out.println("Autores disponibles:");
        for (int i = 0; i < autores.size(); i++) {
            Autor a = autores.get(i);
            System.out.println("  " + (i + 1) + ". " + a.getNombre() + " (" + a.getNacionalidad() + ")");
        }
        System.out.println("  0. Registrar un autor nuevo");
        int opcion = leerEntero("Elige el autor", 0, autores.size());
        return (opcion == 0) ? registrarAutor() : autores.get(opcion - 1);
    }

    private static void registrarLibroFisico() {
        titulo("Registrar libro físico");
        String serie = leerSerieNueva();
        String tituloLibro = leerTexto("Título");
        int cantidad = leerEntero("Cantidad de ejemplares", 1, Integer.MAX_VALUE);
        Date publicacion = leerFecha("Fecha de publicación");
        Autor autor = seleccionarAutor();
        String ubicacion = leerTexto("Ubicación (ej: Estante A-3)");
        int paginas = leerEntero("Número de páginas", 1, Integer.MAX_VALUE);

        Libro libro = new LibroFisico(serie, tituloLibro, cantidad, publicacion, autor, ubicacion, paginas);
        biblioteca.ingresarLibro(libro);
        System.out.println("Libro registrado: " + libro);
    }

    private static void registrarLibroDigital() {
        titulo("Registrar libro digital");
        String serie = leerSerieNueva();
        String tituloLibro = leerTexto("Título");
        int licencias = leerEntero("Número de licencias simultáneas", 1, Integer.MAX_VALUE);
        Date publicacion = leerFecha("Fecha de publicación");
        Autor autor = seleccionarAutor();
        String formato = leerTexto("Formato (ej: PDF, EPUB)");
        double tamano = leerDecimal("Tamaño en MB");
        String url = leerTexto("URL de descarga");

        Libro libro = new LibroDigital(serie, tituloLibro, licencias, publicacion, autor, formato, tamano, url);
        biblioteca.ingresarLibro(libro);
        System.out.println("Libro registrado: " + libro);
    }

    /** Usa la sobrecarga ingresarLibro(String, int, boolean) de Biblioteca. */
    private static void ingresarExistencias() {
        titulo("Ingresar más ejemplares/licencias");
        if (biblioteca.getCatalogo().isEmpty()) {
            System.out.println("El catálogo está vacío. Registra un libro primero.");
            return;
        }
        String serie = leerTexto("Número de serie del libro");
        Libro libro = biblioteca.buscarLibroPorSerie(serie);
        if (libro == null) {
            System.out.println("No existe un libro con esa serie.");
            return;
        }
        int cantidad = leerEntero("Cantidad a ingresar", 1, Integer.MAX_VALUE);
        boolean disponibles = leerSiNo("¿Ingresan disponibles?");

        biblioteca.ingresarLibro(serie, cantidad, disponibles);
        System.out.println("Resultado: " + libro);
    }

    private static void verCatalogo() {
        titulo("Catálogo");
        imprimirCatalogo(biblioteca);
    }

    // =====================================================================
    //  USUARIOS
    // =====================================================================

    private static void registrarUsuario() {
        titulo("Registrar usuario");
        String id;
        while (true) {
            id = leerTexto("ID del usuario");
            if (biblioteca.buscarUsuarioPorId(id) == null) {
                break;
            }
            System.out.println("  Ya existe un usuario con ese ID.");
        }
        String nombre = leerTexto("Nombre");
        String correo;
        while (true) {
            correo = leerTexto("Correo");
            if (correo.contains("@") && correo.contains(".")) {
                break;
            }
            System.out.println("  Correo inválido (ej: nombre@dominio.com).");
        }

        Usuario usuario = new Usuario(id, nombre, correo);
        biblioteca.registrarUsuario(usuario);
        System.out.println("Usuario registrado: " + usuario);
    }

    private static void verUsuariosYPrestamos() {
        titulo("Usuarios y préstamos");
        List<Usuario> usuarios = biblioteca.getUsuarios();
        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
            return;
        }
        for (Usuario u : usuarios) {
            System.out.println("  " + u + " | activos: " + u.getPrestamosActivos().size()
                    + " | total multas: $" + (long) u.calcularTotalMultas());
            for (Prestamo p : u.getPrestamos()) {
                System.out.println("      " + resumenPrestamo(p));
            }
        }
    }

    // =====================================================================
    //  PRÉSTAMOS Y DEVOLUCIONES
    // =====================================================================

    private static void realizarPrestamo() {
        titulo("Realizar préstamo");
        if (biblioteca.getUsuarios().isEmpty() || biblioteca.getCatalogo().isEmpty()) {
            System.out.println("Necesitas al menos un usuario y un libro registrados.");
            return;
        }
        String idUsuario = leerTexto("ID del usuario");
        Usuario usuario = biblioteca.buscarUsuarioPorId(idUsuario);
        if (usuario == null) {
            System.out.println("No existe un usuario con ese ID.");
            return;
        }
        if (!usuario.puedePedirPrestado()) {
            System.out.println("El usuario no puede pedir prestado: ya tiene "
                    + Usuario.MAX_PRESTAMOS_ACTIVOS + " préstamos activos o alguno está vencido.");
            return;
        }

        String linea = leerTexto("Números de serie separados por coma (ej: LF-001, LD-001)");
        List<String> series = new ArrayList<>();
        for (String s : linea.split(",")) {
            String limpio = s.trim();
            if (!limpio.isEmpty()) {
                series.add(limpio);
            }
        }
        int dias = leerEntero("Días de préstamo", 1, 365);

        String idPrestamo = String.format("P-%03d", ++contadorPrestamos);
        Prestamo prestamo = biblioteca.realizarPrestamo(idPrestamo, usuario, series, dias);
        if (prestamo == null) {
            contadorPrestamos--; // no se creó: no gastamos el consecutivo
            System.out.println("No se pudo realizar el préstamo. Verifica que todos los libros existan,");
            System.out.println("estén disponibles y no estén repetidos.");
            return;
        }
        System.out.println("Préstamo " + idPrestamo + " registrado para " + usuario.getNombre() + ":");
        for (Libro libro : prestamo.getLibrosPrestados()) {
            System.out.println("  - " + libro.getTitulo());
        }
        System.out.println("  Devolución esperada: " + formatear(prestamo.getFechaDevolucionEsperada()));
    }

    private static void registrarDevolucion() {
        titulo("Registrar devolución");
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : biblioteca.getPrestamos()) {
            if (p.estaActivo()) {
                activos.add(p);
            }
        }
        if (activos.isEmpty()) {
            System.out.println("No hay préstamos activos.");
            return;
        }
        System.out.println("Préstamos activos:");
        for (Prestamo p : activos) {
            System.out.println("  " + resumenPrestamo(p));
        }

        String id = leerTexto("ID del préstamo a devolver").toUpperCase();
        Prestamo prestamo = biblioteca.buscarPrestamoPorId(id);
        if (prestamo == null || !prestamo.estaActivo()) {
            System.out.println("No existe un préstamo activo con ese ID.");
            return;
        }
        Date fecha = leerFechaOpcional("Fecha de devolución");

        biblioteca.devolverPrestamo(id, fecha);
        System.out.println("Préstamo " + id + " -> estado: " + prestamo.getEstado()
                + " | multa: $" + (long) prestamo.getMulta());
    }

    // =====================================================================
    //  DATOS DE EJEMPLO
    // =====================================================================

    private static void cargarDatosEjemplo() {
        titulo("Cargar datos de ejemplo");
        for (String serie : new String[]{"LF-001", "LF-002", "LD-001", "LD-002"}) {
            if (biblioteca.buscarLibroPorSerie(serie) != null) {
                System.out.println("Ya existe un libro con la serie " + serie
                        + ". Los datos de ejemplo ya están cargados o usan códigos repetidos.");
                return;
            }
        }
        if (biblioteca.buscarUsuarioPorId("U-001") != null || biblioteca.buscarUsuarioPorId("U-002") != null) {
            System.out.println("Ya existen los usuarios U-001/U-002. No se cargan los ejemplos.");
            return;
        }

        Autor garciaMarquez = new Autor("Gabriel García Márquez", fecha(1927, 3, 6), "Colombiana");
        Autor allende = new Autor("Isabel Allende", fecha(1942, 8, 2), "Chilena");
        Autor martin = new Autor("Robert C. Martin", fecha(1952, 12, 5), "Estadounidense");
        autores.addAll(Arrays.asList(garciaMarquez, allende, martin));

        biblioteca.ingresarLibro(new LibroFisico("LF-001", "Cien años de soledad", 2,
                fecha(1967, 5, 30), garciaMarquez, "Estante A-3", 471));
        biblioteca.ingresarLibro(new LibroFisico("LF-002", "La casa de los espíritus", 1,
                fecha(1982, 1, 1), allende, "Estante B-1", 433));
        biblioteca.ingresarLibro(new LibroDigital("LD-001", "Clean Code", 2,
                fecha(2008, 8, 1), martin, "PDF", 8.5, "https://biblioteca.edu/clean-code.pdf"));
        biblioteca.ingresarLibro(new LibroDigital("LD-002", "El amor en los tiempos del cólera", 1,
                fecha(1985, 1, 1), garciaMarquez, "EPUB", 2.1, "https://biblioteca.edu/amor-colera.epub"));

        biblioteca.registrarUsuario(new Usuario("U-001", "Laura Gómez", "laura@correo.com"));
        biblioteca.registrarUsuario(new Usuario("U-002", "Carlos Pérez", "carlos@correo.com"));

        System.out.println("Cargados: 3 autores, 4 libros (LF-001, LF-002, LD-001, LD-002)"
                + " y 2 usuarios (U-001, U-002).");
    }

    /**
     * Demostración automática (sin teclado) en su propia Biblioteca, para ver
     * polimorfismo, sobrecarga y el flujo prestar/devolver de un vistazo.
     */
    private static void demostracionAutomatica() {
        Biblioteca demo = new Biblioteca("Biblioteca de demostración");

        Autor garciaMarquez = new Autor("Gabriel García Márquez", fecha(1927, 3, 6), "Colombiana");
        Autor allende = new Autor("Isabel Allende", fecha(1942, 8, 2), "Chilena");
        Autor martin = new Autor("Robert C. Martin", fecha(1952, 12, 5), "Estadounidense");

        // POLIMORFISMO: variables de tipo Libro con objetos LibroFisico/LibroDigital
        Libro cienAnios = new LibroFisico("LF-001", "Cien años de soledad", 2,
                fecha(1967, 5, 30), garciaMarquez, "Estante A-3", 471);
        Libro casaEspiritus = new LibroFisico("LF-002", "La casa de los espíritus", 1,
                fecha(1982, 1, 1), allende, "Estante B-1", 433);
        Libro cleanCode = new LibroDigital("LD-001", "Clean Code", 2,
                fecha(2008, 8, 1), martin, "PDF", 8.5, "https://biblioteca.edu/clean-code.pdf");
        Libro amorColera = new LibroDigital("LD-002", "El amor en los tiempos del cólera", 1,
                fecha(1985, 1, 1), garciaMarquez, "EPUB", 2.1, "https://biblioteca.edu/amor-colera.epub");

        demo.ingresarLibro(cienAnios);
        demo.ingresarLibro(casaEspiritus);
        demo.ingresarLibro(cleanCode);
        demo.ingresarLibro(amorColera);

        titulo("DEMO - Catálogo inicial (List<Libro> con físicos y digitales)");
        imprimirCatalogo(demo);

        titulo("DEMO - Sobrecarga: ingresarLibro(String, int, boolean) vs ingresarLibro(Libro)");
        demo.ingresarLibro("LF-002", 2, true);
        System.out.println("  " + demo.buscarLibroPorSerie("LF-002"));

        Usuario laura = new Usuario("U-001", "Laura Gómez", "laura@correo.com");
        Usuario carlos = new Usuario("U-002", "Carlos Pérez", "carlos@correo.com");
        demo.registrarUsuario(laura);
        demo.registrarUsuario(carlos);

        titulo("DEMO - Préstamos");
        Prestamo p1 = demo.realizarPrestamo("P-001", laura, Arrays.asList("LF-001", "LD-001"), 7);
        mostrarResultado("P-001 (Laura: Cien años de soledad + Clean Code)", p1);
        Prestamo p2 = demo.realizarPrestamo("P-002", carlos, Arrays.asList("LD-002"), 7);
        mostrarResultado("P-002 (Carlos: El amor en los tiempos del cólera)", p2);
        Prestamo p3 = demo.realizarPrestamo("P-003", laura, Arrays.asList("LD-002"), 7);
        mostrarResultado("P-003 (Laura: LD-002, sin licencias libres)", p3);

        System.out.println("\nCatálogo después de los préstamos:");
        imprimirCatalogo(demo);

        titulo("DEMO - Devoluciones");
        demo.devolverPrestamo("P-001", sumarDias(p1.getFechaDevolucionEsperada(), 5));
        System.out.println("P-001 devuelto con 5 días de retraso -> estado: " + p1.getEstado()
                + " | multa: $" + (long) p1.getMulta());
        demo.devolverPrestamo("P-002", sumarDias(p2.getFechaDevolucionEsperada(), -1));
        System.out.println("P-002 devuelto a tiempo -> estado: " + p2.getEstado()
                + " | multa: $" + (long) p2.getMulta());

        System.out.println("\nCatálogo después de las devoluciones:");
        imprimirCatalogo(demo);

        titulo("DEMO - Sobrecarga en LibroFisico: devolver() vs devolver(String estado)");
        LibroFisico casa = (LibroFisico) demo.buscarLibroPorSerie("LF-002");
        casa.prestar();
        casa.devolver("Deteriorado");
        System.out.println("  " + casa);
    }

    // =====================================================================
    //  LECTURA VALIDADA DE DATOS (el administrador no puede romper el programa)
    // =====================================================================

    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje + ": ");
            String texto = entrada.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("  El valor no puede estar vacío.");
        }
    }

    private static int leerEntero(String mensaje, int min, int max) {
        while (true) {
            String texto = leerTexto(mensaje);
            try {
                int valor = Integer.parseInt(texto);
                if (valor >= min && valor <= max) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cae al mensaje de abajo
            }
            if (max == Integer.MAX_VALUE) {
                System.out.println("  Ingresa un número entero mayor o igual a " + min + ".");
            } else {
                System.out.println("  Ingresa un número entero entre " + min + " y " + max + ".");
            }
        }
    }

    private static double leerDecimal(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje).replace(',', '.');
            try {
                double valor = Double.parseDouble(texto);
                if (valor > 0) {
                    return valor;
                }
            } catch (NumberFormatException e) {
                // cae al mensaje de abajo
            }
            System.out.println("  Ingresa un número mayor que 0 (ej: 8.5).");
        }
    }

    private static boolean leerSiNo(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje + " (s/n)").toLowerCase();
            if (texto.equals("s") || texto.equals("si") || texto.equals("sí")) {
                return true;
            }
            if (texto.equals("n") || texto.equals("no")) {
                return false;
            }
            System.out.println("  Responde s o n.");
        }
    }

    private static Date leerFecha(String mensaje) {
        while (true) {
            String texto = leerTexto(mensaje + " [dd/mm/aaaa]");
            try {
                return aDate(LocalDate.parse(texto, FORMATO_FECHA));
            } catch (DateTimeParseException e) {
                System.out.println("  Fecha inválida. Usa el formato dd/mm/aaaa (ej: 25/12/2024).");
            }
        }
    }

    /** Igual que leerFecha, pero si el administrador solo pulsa Enter se usa la fecha de hoy. */
    private static Date leerFechaOpcional(String mensaje) {
        while (true) {
            System.out.print(mensaje + " [dd/mm/aaaa, Enter = hoy]: ");
            String texto = entrada.nextLine().trim();
            if (texto.isEmpty()) {
                return new Date();
            }
            try {
                return aDate(LocalDate.parse(texto, FORMATO_FECHA));
            } catch (DateTimeParseException e) {
                System.out.println("  Fecha inválida. Usa el formato dd/mm/aaaa (ej: 25/12/2024).");
            }
        }
    }

    private static String leerSerieNueva() {
        while (true) {
            String serie = leerTexto("Número de serie (ej: LF-001)");
            if (biblioteca.buscarLibroPorSerie(serie) == null) {
                return serie;
            }
            System.out.println("  Ya existe un libro con esa serie.");
        }
    }

    // =====================================================================
    //  AUXILIARES DE PRESENTACIÓN
    // =====================================================================

    private static void imprimirCatalogo(Biblioteca b) {
        List<Libro> catalogo = b.getCatalogo();
        if (catalogo.isEmpty()) {
            System.out.println("  (catálogo vacío)");
            return;
        }
        for (Libro libro : catalogo) {
            // estaDisponible() y toString() se resuelven según el tipo REAL del objeto
            System.out.println("  " + libro.getClass().getSimpleName() + " -> " + libro
                    + " | disponible: " + libro.estaDisponible());
        }
    }

    private static String resumenPrestamo(Prestamo p) {
        StringBuilder titulos = new StringBuilder();
        for (Libro libro : p.getLibrosPrestados()) {
            if (titulos.length() > 0) {
                titulos.append(", ");
            }
            titulos.append(libro.getTitulo());
        }
        String vencido = p.estaVencido() ? " [VENCIDO]" : "";
        return p.getIdPrestamo() + " | " + p.getEstado() + vencido + " | libros: " + titulos
                + " | vence: " + formatear(p.getFechaDevolucionEsperada())
                + " | multa: $" + (long) p.getMulta();
    }

    private static void mostrarResultado(String descripcion, Prestamo prestamo) {
        if (prestamo == null) {
            System.out.println("  " + descripcion + " -> NO SE PUDO REALIZAR");
        } else {
            System.out.println("  " + descripcion + " -> OK (" + prestamo.getLibrosPrestados().size()
                    + " libro(s), estado: " + prestamo.getEstado() + ")");
        }
    }

    private static void titulo(String texto) {
        System.out.println("\n=== " + texto + " ===");
    }

    private static Date aDate(LocalDate fecha) {
        return Date.from(fecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private static Date fecha(int anio, int mes, int dia) {
        return aDate(LocalDate.of(anio, mes, dia));
    }

    private static Date sumarDias(Date base, int dias) {
        return new Date(base.getTime() + dias * MILLIS_POR_DIA);
    }

    private static String formatear(Date fecha) {
        return FORMATO_FECHA.format(fecha.toInstant().atZone(ZoneId.systemDefault()));
    }
}