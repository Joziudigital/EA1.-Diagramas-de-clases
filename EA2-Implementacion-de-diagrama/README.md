## Presentación de la Actividad
La programación orientada a objetos (POO) es un paradigma que organiza el software en objetos: unidades que reúnen estado (atributos) y comportamiento (métodos) y que representan las entidades del problema que se desea resolver. Más allá de la sintaxis de un lenguaje, la POO se apoya en cuatro pilares (abstracción, encapsulamiento, herencia y polimorfismo) que permiten construir programas más claros, reutilizables y fáciles de mantener. A ellos se suman los principios de diseño SOLID, sistematizados por Martin (2003), que orientan la manera en que las clases deben relacionarse para que un sistema pueda cambiar sin deteriorarse.
En este contexto, la actividad evaluativa consistió en desarrollar, en el lenguaje Java, un sistema de consola para la gestión de una biblioteca. El sistema permite registrar autores, libros físicos y digitales, usuarios y existencias; realizar préstamos y devoluciones; y consultar el catálogo y el historial de préstamos de cada usuario. Su diseño se compone de ocho clases (Autor, Libro, LibroFisico, LibroDigital, Biblioteca, Prestamo, Usuario y Main) que permiten evidenciar el uso de getters y setters, abstracción, encapsulamiento, herencia, polimorfismo, sobrecarga y sobreescritura.
Este documento se organiza de la siguiente manera. Primero se presentan el objetivo general y los objetivos específicos. Luego, en el desarrollo, se describe el proceso de implementación, se muestra la evidencia de ejecución del programa y se explica cómo se aplicaron los pilares de la POO y los principios SOLID. Finalmente, se formulan las conclusiones, con una reflexión sobre lo aprendido y su aporte al desempeño profesional.

##  Tabla de contenido

- [Objetivo general](#objetivo-principal)
- [Objetivos específicos](#objetivos-específicos)
- [Aplicación de los Pilares de la Programación Orientada a Objetos](#aplicación-de-los-pilares-de-la-programación-orientada-a-objetos)
- [Aplicación de los Principios SOLID](#aplicación-de-los-principios-solid)
- [Archivos del proyecto](#archivos-del-proyecto)


## Objetivo Principal

Desarrollar en Java un sistema de gestión de biblioteca que aplique los pilares de la programación orientada a objetos y cuyo diseño sea analizado a la luz de los principios SOLID, con el fin de demostrar la capacidad de modelar un problema real mediante clases, objetos y relaciones entre ellos

## Objetivos Específicos

1.	Modelar las entidades del dominio (autor, libro, biblioteca, usuario y préstamo) mediante clases con atributos y comportamientos propios.
2.	Aplicar encapsulamiento y métodos getters y setters para proteger el estado interno de los objetos y controlar la forma en que se accede a él.
3.	Implementar una jerarquía de herencia basada en una clase abstracta (Libro) y sus especializaciones (LibroFisico y LibroDigital), aplicando el principio de abstracción.
4.	Emplear el polimorfismo, mediante sobrecarga y sobreescritura de métodos, para tratar de manera uniforme distintos tipos de libros.
5.	Verificar el funcionamiento del programa mediante evidencias de ejecución y evaluar el cumplimiento de los principios SOLID en el diseño obtenido.


## Aplicación de los Pilares de la Programación Orientada a Objetos

**Getters y Setters**
Los getters y setters son métodos públicos que leen y modifican atributos privados. Están presentes en Autor (nombre, fecha de nacimiento y nacionalidad), Usuario, Prestamo y Libro, y permiten que el resto del programa —por ejemplo, Main— consulte y actualice datos sin acceder directamente a los campos. En Main.ingresarExistencias() se usa libro.setCantidad(libro.getCantidad() + cantidadNueva) para incrementar la cantidad de un libro. Además, no todos los atributos tienen setter: copiasDisponibles en LibroFisico y licenciasEnUso en LibroDigital solo tienen getter, porque su valor debe cambiar únicamente a través de los métodos que encierran la regla de negocio (prestar(), devolver(), descargar() y liberarLicencia()).
**Abstracción**
La abstracción consiste en modelar solo lo esencial de un concepto y dejar de lado los detalles particulares. Libro es una clase abstracta: reúne lo que todo libro tiene (número de serie, título, cantidad, fecha de publicación y autor) y declara qué debe poder hacer (informar si está disponible) sin decir cómo (Figura 12). Como la regla de disponibilidad cambia según el tipo de libro, su implementación se delega en las subclases. Además, no tiene sentido crear un «libro genérico»; por eso la clase es abstracta y no puede instanciarse.
**Encapsulamiento**
El encapsulamiento protege el estado interno de un objeto y solo permite modificarlo mediante métodos controlados. En todas las clases los atributos son private, y las listas internas (catalogo, prestamos, historialPrestamos y librosPrestados) son final, por lo que la referencia no puede reemplazarse. Esto coincide con la recomendación de minimizar la accesibilidad de las clases y de sus miembros (Bloch, 2018). El caso más claro está en LibroFisico (Figura 13): el atributo copiasDisponibles es privado y solo cambia mediante prestar(int), que rechaza cantidades no positivas o mayores a las disponibles, y mediante devolver(int), que impide superar la cantidad total gracias a Math.min. Así, el objeto nunca queda en un estado inválido, por ejemplo con copias negativas.
**Polimorfismo**
El polimorfismo permite tratar objetos de distintas clases a través de una interfaz común, con un comportamiento propio en cada caso. En el sistema se manifiesta de dos formas. La primera es el polimorfismo de subtipos, o dinámico: tanto Biblioteca (List<Libro>) como Main manejan referencias de tipo Libro, y la llamada libro.estaDisponible() ejecuta la versión de LibroFisico o de LibroDigital según el tipo real del objeto en tiempo de ejecución. La segunda es la coincidencia de patrones en switch, que se emplea cuando hay que ejecutar acciones propias de cada subtipo: prestar() o descargar() al realizar un préstamo, y devolver() o liberarLicencia() al registrar una devolución.
**Sobrecarga**
La sobrecarga, también llamada polimorfismo estático, consiste en definir varios métodos con el mismo nombre y distinta lista de parámetros; el compilador elige cuál invocar según los argumentos. En el proyecto aparece en LibroFisico (prestar() y prestar(int); devolver() y devolver(int)), en LibroDigital (descargar() y descargar(String)) y en Biblioteca (ingresarLibro(Libro) e ingresarLibro(String, int, boolean)). Las versiones sin parámetros delegan en las otras con un valor por defecto: prestar() llama a prestar(1) y descargar() llama a descargar(this.formato), lo que evita duplicar la lógica.
**Sobreescritura**
La sobreescritura ocurre cuando una subclase redefine un método heredado con la misma firma. LibroFisico y LibroDigital sobreescriben, con @Override, el método abstracto estaDisponible(): el primero devuelve copiasDisponibles > 0 y el segundo licenciasEnUso < licenciasSimultaneas. También sobreescriben toString(), heredado de Object, al igual que Usuario, para ofrecer una representación legible de cada objeto. La anotación @Override hace que el compilador verifique que la firma coincide con la del método de la superclase.

## Aplicación de los Principios SOLID

SOLID es un acrónimo que agrupa cinco principios de diseño orientado a objetos: responsabilidad única, abierto/cerrado, sustitución de Liskov, segregación de interfaces e inversión de dependencias.

**Principio de Responsabilidad Única**
Cada clase debe tener un solo motivo para cambiar. En el proyecto, Autor solo conoce los datos de un autor; Usuario gestiona su historial; Prestamo modela el ciclo de un préstamo (fechas, estado y multa); Biblioteca coordina el catálogo y el registro de préstamos, y Main se limita a la interacción con la persona usuaria. El principio se cumple en buena medida, con una excepción: Main también contiene reglas de negocio, como el plazo de siete días del préstamo, que podrían trasladarse a Biblioteca o a Prestamo.

**Principio Abierto/Cerrado**
Según este principio, las clases deben estar abiertas a la extensión y cerradas a la modificación (Meyer, 1997). La jerarquía de libros lo cumple: para incorporar un nuevo formato, como un audiolibro, basta con crear una subclase de Libro que implemente estaDisponible(); Libro y Biblioteca no cambian. Sin embargo, los switch con patrones de Main (en realizarPrestamo, registrarDevolucion e ingresarExistencias) sí tendrían que modificarse. Declarar en Libro operaciones abstractas como prestar() y devolver() eliminaría ese acoplamiento.

**Principio de Sustitución de Liskov**
Los objetos de una subclase deben poder usarse en lugar de los de su superclase sin alterar el comportamiento esperado del programa (Liskov & Wing, 1994). Esto se comprueba en Biblioteca.buscarLibroPorSerie, que devuelve un Libro, y en Main, que invoca estaDisponible() sin conocer el tipo real. Ambas subclases respetan el contrato de la superclase: devuelven un boolean, no lanzan excepciones ni exigen condiciones adicionales para ser consultadas.


**Principio de Segregación de Interfaces**
Este principio establece que ninguna clase debe verse obligada a depender de métodos que no utiliza. Aunque el proyecto no define interfaces de Java, Libro expone solo el comportamiento común (estaDisponible() y los datos básicos), y las operaciones propias de cada formato se declaran únicamente en la subclase correspondiente; así, LibroDigital no hereda un método prestar() que no le corresponde. El principio se aplica de forma parcial: definir interfaces como Prestable y Descargable lo haría explícito.

**Principio de Inversión de Dependencias**
Los módulos de alto nivel no deben depender de los de bajo nivel, sino de abstracciones. Biblioteca guarda un List<Libro> y Prestamo guarda también un List<Libro>; es decir, ambas dependen de la abstracción Libro y no de LibroFisico o LibroDigital. La aplicación es parcial, porque Main instancia directamente las clases concretas (new Biblioteca(...), new LibroFisico(...)) y no existe una abstracción para Biblioteca.


## Archivos del proyecto 
* **Repositorio:** [Enlace a GitHub](https://github.com/Joziudigital/EA1.-Diagramas-de-clases/tree/main/EA2-Implementacion-de-diagrama)
* **Video de presentación:** [Presentación](https://drive.google.com/file/d/17m4yNzXCn0bhhmhg8MVeNv-fBCabxlXN/view?usp=sharing )
* **Documento Word de Drive:** [EA1. Diagrama de clases](https://docs.google.com/document/d/1T2Y80jZjzlGn-8hQxcv0ZSURwsd-4Lzo/edit?usp=drive_link&ouid=110524674555231772836&rtpof=true&sd=true)

