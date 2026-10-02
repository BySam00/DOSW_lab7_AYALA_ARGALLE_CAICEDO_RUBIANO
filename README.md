# DOSW_lab6_AYALA_ARGALLE_CAICEDO_RUBIANO

### Equipo de trabajo 
- Juanita Rubiano
- Javier Caicedo 
- Samuel Argalle 
- Pedro Ayala



## PREGUNTAS TEORICAS --- PARTE 2  

* ¿Para qué sirve el paquete Controller en la estructura Spring Boot?
* ¿Para qué sirve el paquete Service en la estructura Spring Boot?
* ¿Para qué sirve el paquete Model en la estructura Spring Boot?
* ¿Para qué sirve el paquete Repository en la estructura Spring Boot?
* ¿Para qué sirve el paquete Entity en la estructura Spring Boot?
* ¿Para qué sirve el paquete DTO en la estructura Spring Boot?
* ¿Para qué sirve el paquete Exception en la estructura Spring Boot?

RESPUESTAS:

1. El paquete Controller contiene las clases encargadas de recibir y gestionar las solicitudes HTTP (peticiones REST o MVC) provenientes del cliente. Estas clases, generalmente anotadas con @RestController o @Controller, exponen los endpoints de la aplicación, delegan la lógica de negocio a la capa Service y devuelven la respuesta correspondiente (JSON, XML, vistas, etc.) al cliente.

2. El paquete Service contiene la lógica de negocio de la aplicación. Actúa como intermediario entre el Controller y el Repository, procesando, validando y transformando los datos antes de persistirlos o devolverlos. Las clases de este paquete suelen anotarse con @Service.

3. El paquete Model agrupa las clases que representan los objetos o el estado de los datos que maneja la aplicación. Puede incluir clases de dominio, POJOs (Plain Old Java Objects) o, en algunos proyectos, se usa como sinónimo del paquete Entity para representar la información con la que trabaja el sistema.

4. El paquete Repository contiene las interfaces encargadas del acceso a la base de datos (capa de persistencia). Normalmente extienden interfaces de Spring Data como JpaRepository o CrudRepository, lo que permite realizar operaciones CRUD (crear, leer, actualizar, eliminar) y consultas personalizadas sin necesidad de implementar manualmente el código SQL.

5. El paquete Entity contiene las clases anotadas con @Entity, que representan las tablas de la base de datos mediante el mapeo objeto-relacional (ORM) que provee JPA/Hibernate. Cada instancia de una entidad corresponde a una fila de la tabla asociada.

6. El paquete DTO (Data Transfer Object) contiene clases utilizadas para transportar datos entre las distintas capas de la aplicación o hacia el cliente, sin exponer directamente las entidades de la base de datos. Esto permite controlar qué información se envía o recibe, mejorar la seguridad y desacoplar el modelo interno de la representación externa de los datos

7. El paquete Exception agrupa las clases de excepciones personalizadas y los manejadores de errores de la aplicación (por ejemplo, mediante @ControllerAdvice y @ExceptionHandler). Su propósito es centralizar y estandarizar el manejo de errores, permitiendo devolver respuestas claras y consistentes al cliente cuando ocurre una falla.

bibliografia:

Spring. (s. f.). Spring Boot reference documentation. VMware. https://docs.spring.io/spring-boot/index.html

Spring. (s. f.). Spring Data JPA - Reference documentation. VMware. https://docs.spring.io/spring-data/jpa/reference/

Oracle. (s. f.). Java Persistence API (JPA). Oracle Corporation. https://www.oracle.com/java/technologies/persistence-jsp.html

Baeldung. (s. f.). A guide to spring boot RESTful web services. Baeldung. https://www.baeldung.com/spring-boot-start



## DIAGRAMA DE CLASES -- PARTE 3

El diagrama de clases se construyó a partir de una propuesta inicial que contempla las clases mínimas necesarias para cumplir con los requerimientos relacionados con **User, Worker y Authentication**.

Cada clase cuenta con su respectiva descripción, así como con los atributos necesarios para representar su funcionalidad. Además, se especifican los modificadores de acceso correspondientes para cada atributo. Las relaciones entre las clases también se encuentran definidas y cuentan con su respectivo nombre, permitiendo representar de manera clara la interacción y dependencia entre los diferentes componentes del sistema.

![Imagen del diagrama ](/oficioya/src/main/resources/docs/images/image.png)


## DESCRIPCION DEL CRUDE -- PARTE 4 
### Crear (Create)
El registro de un trabajador parte de los datos personales del usuario: nombre, correo, teléfono, contraseña y foto, los cuales se almacenan en User. Si la persona además desea ofrecer un oficio, se completan los datos propios de Worker: oficio principal, tarifa, zona de cobertura y disponibilidad. El sistema crea el User y le asocia el Worker correspondiente. Si la persona ya cuenta con un User registrado como contratante, no se crea una cuenta nueva; simplemente se le asocia el rol de Worker a la cuenta existente. Todo trabajador se crea por defecto con estado *Activo*.

### Leer (Read)
La consulta de información puede realizarse de forma individual (perfil de un trabajador específico) o en listado (por ejemplo, trabajadores disponibles en una zona). En ambos casos, la respuesta combina los datos públicos de User (nombre, foto) con los datos propios de Worker (oficio principal, oficios secundarios, tarifa, disponibilidad), excluyendo información sensible como correo, teléfono y contraseña.

### Actualizar (Update)
Un trabajador puede modificar los atributos de su perfil laboral (tarifa, oficio principal, oficios secundarios, disponibilidad), lo cual afecta únicamente a Worker. Los cambios sobre datos personales (nombre, foto) afectan a User de forma independiente. Como regla de negocio, *ningún atributo de Worker puede modificarse si su estado es Inactivo*.

### Eliminar (Delete → Inactivar)
No existe eliminación física de un trabajador. En su lugar, se provee una operación que cambia el estado de Worker de *Activo* a *Inactivo*. El User asociado no se ve afectado: la persona conserva su cuenta y puede seguir operando como contratante; únicamente su perfil de trabajador deja de estar activo y de aparecer en las búsquedas.


# PARTE 5

![swagger](/oficioya/src/main/resources/docs/images/swagger.png)
