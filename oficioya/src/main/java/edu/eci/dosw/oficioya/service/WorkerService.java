package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.dto.WorkerRegistrationDTO;
import edu.eci.dosw.oficioya.dto.WorkerResponseDTO;
import edu.eci.dosw.oficioya.dto.WorkerUpdateDTO;
import edu.eci.dosw.oficioya.exception.InactiveWorkerException;
import edu.eci.dosw.oficioya.exception.InvalidDataException;
import edu.eci.dosw.oficioya.exception.ResourceNotFoundException;
import edu.eci.dosw.oficioya.model.Disponibility;
import edu.eci.dosw.oficioya.model.Job;
import edu.eci.dosw.oficioya.model.User;
import edu.eci.dosw.oficioya.model.Worker;
import edu.eci.dosw.oficioya.model.WorkZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.slf4j.LoggerFactory;

@Service
public class WorkerService {

    private final Map<Integer, Worker> workers = new ConcurrentHashMap<>();
    private final Map<String, User> usersByEmail = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);
    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);

    public WorkerService() {
        seedInitialData();
    }

    private void seedInitialData() {
        // Trabajador 1 (Activo) usando composición: User contiene datos personales y
        // Worker los laborales
        int idU1 = idGenerator.getAndIncrement();
        User u1 = new User(idU1, "Carlos Perez", "carlos.perez@ejemplo.com", 300123456, "clave123", null);
        usersByEmail.put(u1.getCorreo().toLowerCase(), u1);

        int idW1 = 1;
        Worker w1 = new Worker(idW1, u1, 50000, new Job("Plomero", true),
                new WorkZone(1, "Bogota", "Usaquen", "Cedritos"));
        w1.setState(true);
        w1.setFinishedJobs(12);
        w1.getDisponibilities().add(new Disponibility(true));
        u1.setWorker(w1);
        workers.put(idW1, w1);

        // Trabajador 2 (Activo)
        int idU2 = idGenerator.getAndIncrement();
        User u2 = new User(idU2, "Ana Gomez", "ana.gomez@ejemplo.com", 310987654, "segura456", null);
        usersByEmail.put(u2.getCorreo().toLowerCase(), u2);

        int idW2 = 2;
        Worker w2 = new Worker(idW2, u2, 65000, new Job("Electricista", true),
                new WorkZone(2, "Bogota", "Chapinero", "Antiguo Country"));
        w2.setState(true);
        w2.setFinishedJobs(25);
        w2.getDisponibilities().add(new Disponibility(true));
        u2.setWorker(w2);
        workers.put(idW2, w2);

        // Trabajador 3 (Inactivo para pruebas de regla de negocio)
        int idU3 = idGenerator.getAndIncrement();
        User u3 = new User(idU3, "Pedro Ramirez", "pedro.ramirez@ejemplo.com", 320112233, "inactivo789", null);
        usersByEmail.put(u3.getCorreo().toLowerCase(), u3);

        int idW3 = 3;
        Worker w3 = new Worker(idW3, u3, 40000, new Job("Carpintero", false),
                new WorkZone(3, "Bogota", "Suba", "La Colina"));
        w3.setState(false); // Inactivo
        w3.setFinishedJobs(5);
        u3.setWorker(w3);
        workers.put(idW3, w3);

        // Usuario contratante previo (sin Worker todavía)
        int idU4 = idGenerator.getAndIncrement();
        User u4 = new User(idU4, "Maria Lopez", "maria.lopez@ejemplo.com", 315443322, "contratante2026", null);
        usersByEmail.put(u4.getCorreo().toLowerCase(), u4);
    }

    /**
     * Listar trabajadores con filtros opcionales (zona de cobertura y estado
     * activo).
     * Por defecto excluye inactivos de las búsquedas públicas según el
     * requerimiento.
     */
    public List<WorkerResponseDTO> findAll(String zona, Boolean soloActivos) {
        log.debug("Buscando trabajadores con zona: {} y soloActivos: {}", zona, soloActivos);
        return workers.values().stream()
                .filter(w -> {
                    if (soloActivos != null && soloActivos && !w.isState()) {
                        log.warn("Trabajador {} inactivo", w.getId());
                        return false;
                    }
                    if (zona != null && !zona.isBlank()) {
                        if (w.getWorkZone() == null)
                            return false;
                        String barrio = w.getWorkZone().getBarrio() != null ? w.getWorkZone().getBarrio() : "";
                        String localidad = w.getWorkZone().getLocalidad() != null ? w.getWorkZone().getLocalidad() : "";
                        String ciudad = w.getWorkZone().getCiudad() != null ? w.getWorkZone().getCiudad() : "";
                        String query = zona.toLowerCase();
                        return barrio.toLowerCase().contains(query)
                                || localidad.toLowerCase().contains(query)
                                || ciudad.toLowerCase().contains(query);
                    }
                    log.info("Trabajador {} encontrado", w.getId());
                    return true;
                })
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Consultar perfil de un solo trabajador por su ID.
     */
    public WorkerResponseDTO findById(int id) {
        log.debug("Buscando trabajador con id: {}", id);
        Worker worker = workers.get(id);
        if (worker == null) {
            throw new ResourceNotFoundException("Trabajador con id " + id + " no encontrado.");
        }
        
        log.info("trabajador encontrado: {}", id);

        return mapToResponseDTO(worker);
    }

    /**
     * Registrar trabajador (Create).
     * Valida campos obligatorios: Nombre, correo, teléfono, oficio principal y
     * contraseña.
     * Mediante composición: si la persona ya existía como contratante (tenía User
     * pero sin Worker),
     * no se crea una cuenta nueva, simplemente se le asocia el Worker a la cuenta
     * que ya tenía.
     * El trabajador se crea por defecto con estado Activo.
     */
    public WorkerResponseDTO create(WorkerRegistrationDTO dto) {
        log.debug("Registrando trabajador con correo: {}", dto.getCorreo());
        validateRegistrationFields(dto);

        String email = dto.getCorreo().trim().toLowerCase();
        User existingUser = usersByEmail.get(email);

        User user;
        Worker worker;

        if (existingUser != null) {
            // Reutilizamos el User existente
            user = existingUser;
            user.setNombre(dto.getNombre());
            user.setTelefono(dto.getTelefono());
            user.setPasswd(dto.getPasswd());
            if (dto.getFoto() != null) {
                user.setFoto(dto.getFoto());
            }

            if (user.getWorker() != null) {
                worker = user.getWorker();
            } else {
                worker = new Worker();
                worker.setId(idGenerator.getAndIncrement());
                worker.setUser(user);
                user.setWorker(worker);
            }
        } else {
            // Usuario nuevo
            int newUserId = idGenerator.getAndIncrement();
            user = new User(newUserId, dto.getNombre(), email, dto.getTelefono(), dto.getPasswd(), dto.getFoto());
            usersByEmail.put(email, user);

            int newWorkerId = idGenerator.getAndIncrement();
            worker = new Worker();
            worker.setId(newWorkerId);
            worker.setUser(user);
            user.setWorker(worker);
        }

        // Datos propios de Worker
        worker.setPrincipalJob(dto.getPrincipalJob());
        worker.setRate(dto.getRate());
        worker.setSalary(dto.getSalary());
        worker.setWorkZone(dto.getWorkZone());
        if (dto.getSecondaryJobs() != null) {
            worker.setSecondaryJobs(dto.getSecondaryJobs());
        }
        if (dto.getDisponibilities() != null) {
            worker.setDisponibilities(dto.getDisponibilities());
        }
        // Regla: Se crea por defecto con estado Activo
        worker.setState(true);

        workers.put(worker.getId(), worker);
        log.info("Trabajador creado exitosamente con id: {}", worker.getId());
        return mapToResponseDTO(worker);
    }

    /**
     * Actualizar datos del trabajador (Update).
     * Modifica atributos de Worker (tarifa, oficios, disponibilidad) y de User
     * (nombre, foto).
     * Regla importante: Si el Worker está inactivo, NO se le puede actualizar nada.
     */
    public WorkerResponseDTO update(int id, WorkerUpdateDTO dto) {
        log.debug("Actualizando trabajador con id: {}", id);
        Worker worker = workers.get(id);
        if (worker == null) {
            throw new ResourceNotFoundException("Trabajador con id " + id + " no encontrado.");
        }

        // Validación de regla de negocio: no se puede actualizar si está inactivo
        if (!worker.isState()) {
            throw new InactiveWorkerException(
                    "No se puede actualizar la información de un trabajador con estado Inactivo.");
        }

        // Modificaciones propias de Worker
        if (dto.getRate() != null) {
            worker.setRate(dto.getRate());
        }
        if (dto.getSalary() != null) {
            worker.setSalary(dto.getSalary());
        }
        if (dto.getPrincipalJob() != null) {
            worker.setPrincipalJob(dto.getPrincipalJob());
        }
        if (dto.getSecondaryJobs() != null) {
            worker.setSecondaryJobs(dto.getSecondaryJobs());
        }
        if (dto.getWorkZone() != null) {
            worker.setWorkZone(dto.getWorkZone());
        }
        if (dto.getDisponibilities() != null) {
            worker.setDisponibilities(dto.getDisponibilities());
        }

        // Modificaciones que impactan al User mediante composición
        if (worker.getUser() != null) {
            if (dto.getNombre() != null && !dto.getNombre().isBlank()) {
                worker.getUser().setNombre(dto.getNombre());
            }
            if (dto.getFoto() != null) {
                worker.getUser().setFoto(dto.getFoto());
            }
        }
        log.info("Trabajador actualizado exitosamente con id: {}", id);
        return mapToResponseDTO(worker);
    }

    /**
     * Inactivación lógica (Delete -> Inactivar).
     * No borra físicamente al usuario ni al trabajador; cambia state a false.
     * El User no se toca: la persona sigue existiendo en el sistema y puede seguir
     * como contratante.
     */
    public WorkerResponseDTO inactivate(int id) {
        log.debug("Inactivando trabajador con id: {}", id);
        Worker worker = workers.get(id);
        if (worker == null) {
            throw new ResourceNotFoundException("Trabajador con id " + id + " no encontrado.");
        }

        worker.setState(false);
        log.info("Trabajador inactivado exitosamente con id: {}", id);
        return mapToResponseDTO(worker);
    }

    /**
     * Buscar un usuario por correo para autenticación.
     */
    public Optional<User> findUserByEmail(String email) {
        log.debug("Buscando usuario con correo: {}", email);
        if (email == null)
            return Optional.empty();
        log.info("Usuario encontrado con correo: {}", email);
        return Optional.ofNullable(usersByEmail.get(email.trim().toLowerCase()));
    }

    private void validateRegistrationFields(WorkerRegistrationDTO dto) {
        if (dto == null) {
            throw new InvalidDataException("Los datos de registro no pueden ser nulos.");
        }
        if (dto.getNombre() == null || dto.getNombre().isBlank()) {
            throw new InvalidDataException("El campo 'Nombre' es obligatorio.");
        }
        if (dto.getCorreo() == null || dto.getCorreo().isBlank()) {
            throw new InvalidDataException("El campo 'correo' es obligatorio.");
        }
        if (dto.getTelefono() == null || dto.getTelefono() <= 0) {
            throw new InvalidDataException("El campo 'teléfono' es obligatorio y debe ser valido.");
        }
        if (dto.getPrincipalJob() == null || dto.getPrincipalJob().getDescription() == null
                || dto.getPrincipalJob().getDescription().isBlank()) {
            throw new InvalidDataException("El campo 'oficio principal' es obligatorio.");
        }
        if (dto.getPasswd() == null || dto.getPasswd().isBlank()) {
            throw new InvalidDataException("El campo 'contraseña' es obligatorio.");
        }
    }

    public WorkerResponseDTO mapToResponseDTO(Worker worker) {
        WorkerResponseDTO dto = new WorkerResponseDTO();
        dto.setId(worker.getId());
        if (worker.getUser() != null) {
            dto.setNombre(worker.getUser().getNombre());
            dto.setFoto(worker.getUser().getFoto());
        }
        dto.setRate(worker.getRate());
        dto.setSalary(worker.getSalary());
        dto.setFinishedJobs(worker.getFinishedJobs());
        dto.setState(worker.isState());
        dto.setPrincipalJob(worker.getPrincipalJob());
        dto.setSecondaryJobs(worker.getSecondaryJobs());
        dto.setWorkZone(worker.getWorkZone());
        dto.setDisponibilities(worker.getDisponibilities());
        return dto;
    }
}
