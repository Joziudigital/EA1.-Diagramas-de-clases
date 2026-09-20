import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Main {

    private final Scanner sc;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    private final Biblioteca biblioteca = new Biblioteca("Biblioteca Central");
    private final List<Autor> autores = new ArrayList<>();
    private final List<Libro> libros = new ArrayList<>();      // espejo del catálogo para poder listarlo
    private final List<Usuario> usuarios = new ArrayList<>();

    private Main(Scanner sc) {
        this.sc = sc;
    }

    public static void main(String[] args) {
        // try-with-resources: el Scanner se cierra automáticamente al terminar
        try (Scanner sc = new Scanner(System.in)) {
            new Main(sc).ejecutar();
        }
    }

    //MENÚ PRINCIPAL

    private void ejecutar() {
        boolean salir = false;

        System.out.println("   SISTEMA DE GESTIÓN DE BIBLIOTECA            ");
        System.out.println("   Bienvenido/a - " + biblioteca.getNombre());


        while (!salir) {
            mostrarMenu();
            int opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1: registrarAutor();
                    break;
                case 2: registrarLibroFisico();
                    break;
                case 3: registrarLibroDigital();
                    break;
                case 4: ingresarExistencias();
                    break;
                case 5: registrarUsuario();
                    break;
                case 6: realizarPrestamo();
                    break; 
                case 7: registrarDevolucion();
                    break;
                case 8: verCatalogo();
                    break;
                case 9: verUsuariosYPrestamos();
                    break;
                case 10: cargarDatosEjemplo();
                    break; 
                case 0: salir = true;
                default: System.out.println("Opción no válida, intenta de nuevo.");
            }

            if (!salir) {
                pausar();
            }
        }

        System.out.println("\n¡Hasta pronto! 👋");
    }

    private void mostrarMenu() {
        System.out.println("\n------------------------------------------------");
        System.out.println(" 1.  Registrar autor");
        System.out.println(" 2.  Registrar libro físico");
        System.out.println(" 3.  Registrar libro digital");
        System.out.println(" 4.  Ingresar existencias (aumentar stock/licencias)");
        System.out.println(" 5.  Registrar usuario");
        System.out.println(" 6.  Realizar préstamo");
        System.out.println(" 7.  Registrar devolución");
        System.out.println(" 8.  Ver catálogo de libros");
        System.out.println(" 9.  Ver usuarios y sus préstamos");
        System.out.println(" 10. Cargar datos de ejemplo (para probar rápido)");
        System.out.println(" 0.  Salir");
        System.out.println("------------------------------------------------");
    }

    //OPCIONES DEL MENÚ

    private void registrarAutor() {
        titulo("REGISTRAR AUTOR");
        String nombre = leerTexto("Nombre del autor: ");
        String nacionalidad = leerTexto("Nacionalidad: ");
        Autor autor = new Autor(nombre, new Date(), nacionalidad);
        autores.add(autor);
        System.out.println("Autor \"" + nombre + "\" registrado correctamente.");
    }

    private void registrarLibroFisico() {
        titulo("REGISTRAR LIBRO FÍSICO");
        Autor autor = elegirAutorOCrearUno();
        if (autor == null) return;

        String serie = leerTexto("Número de serie: ");
        String titulo = leerTexto("Título: ");
        int cantidad = leerEnteroPositivo("Cantidad de copias: ");
        String ubicacion = leerTexto("Ubicación en el estante: ");

        LibroFisico libro = new LibroFisico(serie, titulo, cantidad, new Date(), autor, ubicacion);
        biblioteca.ingresarLibro(libro);
        libros.add(libro);
        System.out.println("Libro físico \"" + titulo + "\" registrado con " + cantidad + " copia(s).");
    }

    private void registrarLibroDigital() {
        titulo("REGISTRAR LIBRO DIGITAL");
        Autor autor = elegirAutorOCrearUno();
        if (autor == null) return;

        String serie = leerTexto("Número de serie: ");
        String titulo = leerTexto("Título: ");
        String formato = leerTexto("Formato (PDF/EPUB): ");
        int licencias = leerEnteroPositivo("Licencias simultáneas disponibles: ");

        LibroDigital libro = new LibroDigital(serie, titulo, licencias, new Date(), autor, formato, 4.5, licencias);
        biblioteca.ingresarLibro(libro);
        libros.add(libro);
        System.out.println("Libro digital \"" + titulo + "\" registrado con " + licencias + " licencia(s).");
    }

    private void ingresarExistencias() {
        titulo("INGRESAR EXISTENCIAS");
        if (libros.isEmpty()) {
            System.out.println("Todavía no hay libros registrados.");
            return;
        }
        String serie = leerTexto("Número de serie del libro: ");
        Libro libro = biblioteca.buscarLibroPorSerie(serie);
        if (libro == null) {
            System.out.println("No se encontró ningún libro con esa serie.");
            return;
        }

        int cantidadNueva = leerEnteroPositivo("Cantidad a agregar: ");
        libro.setCantidad(libro.getCantidad() + cantidadNueva);

        // Polimorfismo: según el tipo real del libro, se actualiza lo correspondiente
        switch (libro) {
            case LibroFisico fisico -> {
                fisico.devolver(cantidadNueva); // suma copias disponibles
                System.out.println("Se agregaron " + cantidadNueva + " copia(s) físicas a \"" + libro.getTitulo() + "\".");
            }
            case LibroDigital digital -> {
                digital.setLicenciasSimultaneas(digital.getLicenciasSimultaneas() + cantidadNueva);
                System.out.println("Se agregaron " + cantidadNueva + " licencia(s) a \"" + libro.getTitulo() + "\".");
            }
            default -> {
            }
        }
    }

    private void registrarUsuario() {
        titulo("REGISTRAR USUARIO");
        String id = leerTexto("ID de usuario: ");
        String nombre = leerTexto("Nombre: ");
        String email = leerTexto("Email: ");
        Usuario usuario = new Usuario(id, nombre, email);
        usuarios.add(usuario);
        System.out.println("Usuario \"" + nombre + "\" registrado correctamente.");
    }

    private void realizarPrestamo() {
        titulo("REALIZAR PRÉSTAMO");
        if (usuarios.isEmpty()) {
            System.out.println("Primero registra al menos un usuario (opción 5).");
            return;
        }
        if (libros.isEmpty()) {
            System.out.println("Primero registra al menos un libro (opción 2 o 3).");
            return;
        }

        Usuario usuario = elegirUsuario();
        if (usuario == null) return;

        Libro libro = elegirLibro();
        if (libro == null) return;

        // Polimorfismo: no importa el tipo real, se pregunta por la interfaz común
        if (!libro.estaDisponible()) {
            System.out.println("\"" + libro.getTitulo() + "\" no tiene disponibilidad en este momento.");
            return;
        }

        Date hoy = new Date();
        long MILLIS_POR_DIA = 24L * 60 * 60 * 1000;
        Date devolucionEsperada = new Date(hoy.getTime() + 7 * MILLIS_POR_DIA);

        Prestamo prestamo = new Prestamo("P-" + usuario.getId() + "-" + System.currentTimeMillis(), hoy, devolucionEsperada);
        prestamo.agregarLibro(libro);
        prestamo.setEstado("Activo");

        boolean exito;
        switch (libro) {
            case LibroFisico fisico -> exito = fisico.prestar();
            case LibroDigital digital -> exito = digital.descargar();
            default -> exito = false;
        }

        if (exito) {
            biblioteca.registrarPrestamo(prestamo);
            usuario.solicitarPrestamo(prestamo);
            System.out.println("Préstamo registrado. Devolución esperada: " + sdf.format(devolucionEsperada));
        } else {
            System.out.println("No se pudo completar el préstamo (sin disponibilidad).");
        }
    }

    private void registrarDevolucion() {
        titulo("REGISTRAR DEVOLUCIÓN");
        Usuario usuario = elegirUsuario();
        if (usuario == null) return;

        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : usuario.getHistorialPrestamos()) {
            if (!"Devuelto".equals(p.getEstado())) {
                activos.add(p);
            }
        }

        if (activos.isEmpty()) {
            System.out.println("ℹ Este usuario no tiene préstamos activos.");
            return;
        }

        System.out.println("Préstamos activos de " + usuario.getNombre() + ":");
        for (int i = 0; i < activos.size(); i++) {
            Prestamo p = activos.get(i);
            System.out.println(" " + (i + 1) + ". " + p.getIdPrestamo() + " - libros: " + nombresLibros(p));
        }

        int indice = leerEnteroEnRango("Elige el préstamo a devolver: ", 1, activos.size()) - 1;
        Prestamo prestamo = activos.get(indice);

        prestamo.registrarDevolucion(new Date());


        for (Libro libro : prestamo.getLibrosPrestados()) {
            switch (libro) {
                case LibroFisico fisico -> fisico.devolver();
                case LibroDigital digital -> digital.liberarLicencia();
                default -> {
                }
            }
        }

        System.out.println("Devolución registrada para el préstamo " + prestamo.getIdPrestamo() + ".");
        System.out.println("  (Multa calculada: $" + prestamo.calcularMulta() + " - lógica pendiente de definir en Prestamo)");
    }

    private void verCatalogo() {
        titulo("CATÁLOGO DE LIBROS");
        if (libros.isEmpty()) {
            System.out.println(" No hay libros registrados todavía.");
            return;
        }

        for (Libro libro : libros) {
            if (libro == null) {
                continue;
            }
            String tipo = (libro instanceof LibroFisico) ? "Físico" : "Digital";
            String disponibilidad = libro.estaDisponible() ? "Disponible" : "No disponible ";
            Autor autorLibro = libro.getAutor();
            String nombreAutor = (autorLibro != null) ? autorLibro.getNombre() : "Desconocido";
            System.out.println("- [" + tipo + "] " + libro.getTitulo() +
                    " | Autor: " + nombreAutor +
                    " | Serie: " + libro.getNumeroSerie() +
                    " | " + disponibilidad);
        }
    }

    private void verUsuariosYPrestamos() {
        titulo("USUARIOS Y SUS PRÉSTAMOS");
        if (usuarios.isEmpty()) {
            System.out.println(" No hay usuarios registrados todavía.");
            return;
        }
        for (Usuario usuario : usuarios) {
            System.out.println("\n " + usuario.getNombre() + " (" + usuario.getEmail() + ")");
            if (usuario.getHistorialPrestamos().isEmpty()) {
                System.out.println("   Sin préstamos registrados.");
            } else {
                for (Prestamo p : usuario.getHistorialPrestamos()) {
                    System.out.println("   • " + p.getIdPrestamo() + " | estado: " + p.getEstado() +
                            " | libros: " + nombresLibros(p));
                }
            }
        }
    }

    private void cargarDatosEjemplo() {
        titulo("CARGANDO DATOS DE EJEMPLO");

        Autor autor = new Autor("Gabriel García Márquez", new Date(), "Colombiana");
        autores.add(autor);

        LibroFisico fisico = new LibroFisico("F-EJ-001", "Cien años de soledad", 3, new Date(), autor, "Estante A1");
        biblioteca.ingresarLibro(fisico);
        libros.add(fisico);

        LibroDigital digital = new LibroDigital("D-EJ-001", "El amor en los tiempos del cólera", 2, new Date(), autor, "EPUB", 3.2, 2);
        biblioteca.ingresarLibro(digital);
        libros.add(digital);

        Usuario usuario = new Usuario("U-EJ-001", "Ana Torres", "ana.torres@iudigital.com");
        usuarios.add(usuario);

        Autor autor1 = new Autor("Aurelio Baldor", new Date(), "Cubano");
        autores.add(autor1);

        LibroFisico fisico1 = new LibroFisico("F-EJ-002", "El álgebrade Baldor", 3, new Date(), autor1, "Estante A2");
        biblioteca.ingresarLibro(fisico1);
        libros.add(fisico1);

        LibroDigital digital1 = new LibroDigital("D-EJ-002", "El álgebra de Baldor", 4, new Date(), autor1, "EPUB", 3.2, 2);
        biblioteca.ingresarLibro(digital1);
        libros.add(digital1);

        Usuario usuario1 = new Usuario("U-EJ-002", "Susanito Juarez", "susanito@iudigital.com");
        usuarios.add(usuario1);

        System.out.println(" Se cargaron: 2 autor, 2 libro físico, 2 libro digital y 2 usuario de ejemplo.");
        System.out.println(" Ya puedes probar las opciones 6 (préstamo), 7 (devolución) y 8 (catálogo).");
    }


    //UTILIDADES

    private Autor elegirAutorOCrearUno() {
        if (autores.isEmpty()) {
            System.out.println("ℹ No hay autores registrados todavía, vamos a crear uno.");
            registrarAutor();
            if (autores.isEmpty()) return null; // por si algo falló
            return autores.get(autores.size() - 1);
        }

        System.out.println("Autores disponibles:");
        for (int i = 0; i < autores.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + autores.get(i).getNombre());
        }
        System.out.println(" " + (autores.size() + 1) + ". Registrar un autor nuevo");

        int opcion = leerEnteroEnRango("Elige una opción: ", 1, autores.size() + 1);
        if (opcion == autores.size() + 1) {
            registrarAutor();
            return autores.get(autores.size() - 1);
        }
        return autores.get(opcion - 1);
    }

    private Usuario elegirUsuario() {
        if (usuarios.isEmpty()) {
            System.out.println("ℹ No hay usuarios registrados.");
            return null;
        }
        System.out.println("Usuarios:");
        for (int i = 0; i < usuarios.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + usuarios.get(i).getNombre());
        }
        int opcion = leerEnteroEnRango("Elige un usuario: ", 1, usuarios.size());
        return usuarios.get(opcion - 1);
    }

    private Libro elegirLibro() {
        if (libros.isEmpty()) {
            System.out.println("ℹ No hay libros registrados.");
            return null;
        }
        System.out.println("Libros:");
        for (int i = 0; i < libros.size(); i++) {
            Libro l = libros.get(i);
            if (l == null) {
                System.out.println(" " + (i + 1) + ". [No disponible]");
                continue;
            }
            String tipo = (l instanceof LibroFisico) ? "Físico" : "Digital";
            System.out.println(" " + (i + 1) + ". [" + tipo + "] " + l.getTitulo() +
                    " (" + (l.estaDisponible() ? "disponible" : "no disponible") + ")");
        }
        int opcion = leerEnteroEnRango("Elige un libro: ", 1, libros.size());
        return libros.get(opcion - 1);
    }

    private String nombresLibros(Prestamo p) {
        StringBuilder sb = new StringBuilder();
        for (Libro l : p.getLibrosPrestados()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(l.getTitulo());
        }
        return sb.toString();
    }

    private void titulo(String texto) {
        System.out.println("\n== " + texto + " ==");
    }

    private void pausar() {
        System.out.print("\nPresiona Enter para volver al menú...");
        sc.nextLine();
    }


    private String leerTexto(String mensaje) {
        String valor;
        do {
            System.out.print(mensaje);
            valor = sc.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("Este campo no puede estar vacío, intenta de nuevo.");
            }
        } while (valor.isEmpty());
        return valor;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String entrada = sc.nextLine().trim();
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Escribe solo un número, por favor.");
            }
        }
    }

    private int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor > 0) return valor;
            System.out.println("Debe ser un número mayor a 0.");
        }
    }

    private int leerEnteroEnRango(String mensaje, int min, int max) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= min && valor <= max) return valor;
            System.out.println("Elige un número entre " + min + " y " + max + ".");
        }
    }
}