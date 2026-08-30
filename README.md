##  Tabla de contenido

- [Objetivo general](#-objetivo-general)
- [Objetivos específicos](#-objetivos-específicos)
- [Diagrama de clases](#-diagrama-de-clases)
- [Descripción de las clases](#-descripción-de-las-clases-identificadas)
- [Justificación de las relaciones](#-justificación-de-las-relaciones-utilizadas)
- [Cohesión, bajo acoplamiento y SOLID](#-aplicación-de-cohesión-bajo-acoplamiento-y-solid)
- [Archivos del proyecto](#-archivos-del-proyecto)


```mermaid
classDiagram
    direction TB

    class Biblioteca {
        - nombre: String
        + ingresarLibro(numeroSerie: String, cantidad: int, disponibilidad: boolean): void
        + buscarLibroPorSerie(numeroSerie: String): Libro
        + actualizarDisponibilidad(numeroSerie: String, cantidad: int): void
        + registrarPrestamo(prestamo: Prestamo): void
    }

    abstract class Libro {
         # numeroSerie: String
         # titulo: String
         # cantidad: int
         - fechaPublicacion: Date
        + estaDisponible(): boolean
    }

    class LibroFisico {
        - ubicacionEstante: String
        - estadoFisico: String
        - numeroCopiasEnPrestamo: int
    }

    class LibroDigital {
        - formato: String
        - enlaceDescarga: String
        - tamanoArchivoMB: double
        - licenciasSimultaneas: int
    }

    class Autor {
        # nombre: String
        # fechaNacimiento: Date
        # nacionalidad: String
    }

    class Usuario {
        # id: String
        # nombre: String
        # telefono: String
        # email: String
        - habilitadoParaPrestamo: boolean
        + puedeSolicitarPrestamo(): boolean
    }

    class Prestamo {
        # idPrestamo: String
        # fechaPrestamo: Date
        # fechaDevolucionEsperada: Date
        # fechaDevolucionReal: Date
        - multa: double
        - estado: String
        + calcularMulta(): double
        + estaVencido(): boolean
        + registrarDevolucion(fecha: Date): void
    }

    %%Herencia
    Libro <|-- LibroFisico
    Libro <|-- LibroDigital

    %%Composicion
    Biblioteca "1" *-- "0..*" Libro : administra >
    Biblioteca "1" *-- "0..*" Prestamo : gestiona >

    %%Agregacion
    Libro "0..*" o-- "1" Autor : escrito por >
    Prestamo "1" o-- "1..*" Libro : incluye >

    %%Asociacion simple
    Usuario "1" -- "0..*" Prestamo : solicita >

```

