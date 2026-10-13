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
