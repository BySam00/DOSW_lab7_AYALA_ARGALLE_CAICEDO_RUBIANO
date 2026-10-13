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

## PASO 3 

Paso 3. Entidades seleccionadas para persistencia
Del modelo de datos de OficioYa se seleccionaron las siguientes entidades, que cubren
las funcionalidades de Autenticación y de Trabajadores (CRUD):

| Entidad | Funcionalidad | Justificación |
|---|---|---|
| users | Autenticación y Trabajadores | Representa la cuenta de la persona (nombre, foto, correo, teléfono, estado y rol). Es la base del inicio de sesión y del registro de un trabajador. |
| Credentials | Autenticación | Almacena la contraseña cifrada y sus fechas de creación y expiración. Se separa de users para no mezclar datos personales con datos sensibles de seguridad. |
| workers | Trabajadores (CRUD) | Perfil laboral asociado a un usuario (tarifa, salario, estado). Un trabajador no se elimina físicamente: se inactiva. |

Relaciones
| Relación | Cardinalidad | Anotación JPA |
|---|---|---|
| users - workers | 1 a 0..1 | @OneToOne (llave foránea user_id en workers) |
| users - Credentials | 1 a 1 | @OneToOne (llave foránea user_id en Credentials) |

Entidades no seleccionadas en esta etapa
Jobs, catalogues, Materials, work_zones, worker_zones, subscriptions, disponibilities,
requests, reviews, notifications y las tablas de detalle e imágenes pertenecen al modelo
completo, pero no se persisten en este laboratorio. Varias usan llave primaria compuesta, lo
que se aparta de la regla de id autogenerado e incremental del laboratorio.
