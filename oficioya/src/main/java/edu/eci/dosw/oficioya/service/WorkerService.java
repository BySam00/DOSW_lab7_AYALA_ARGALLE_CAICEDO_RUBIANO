package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.dto.WorkerRegistrationDTO;
import edu.eci.dosw.oficioya.dto.WorkerResponseDTO;
import edu.eci.dosw.oficioya.dto.WorkerUpdateDTO;
import edu.eci.dosw.oficioya.entity.User;
import edu.eci.dosw.oficioya.entity.Worker;
import edu.eci.dosw.oficioya.exception.InactiveWorkerException;
import edu.eci.dosw.oficioya.exception.InvalidDataException;
import edu.eci.dosw.oficioya.exception.ResourceNotFoundException;
import edu.eci.dosw.oficioya.mapper.WorkerMapper;
import edu.eci.dosw.oficioya.model.Disponibility;
import edu.eci.dosw.oficioya.model.Job;
import edu.eci.dosw.oficioya.model.UserModel;
import edu.eci.dosw.oficioya.model.WorkerModel;
import edu.eci.dosw.oficioya.model.WorkZone;
import edu.eci.dosw.oficioya.repository.UserRepository;
import edu.eci.dosw.oficioya.repository.WorkerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Transactional
public class WorkerService {

    private static final Logger log = LoggerFactory.getLogger(WorkerService.class);

    private final WorkerRepository workerRepository;
    private final UserRepository userRepository;
    private final WorkerMapper workerMapper;

    private final Map<Integer, WorkerModel> workers = new ConcurrentHashMap<>();
    private final Map<String, UserModel> usersByEmail = new ConcurrentHashMap<>();
    private final AtomicInteger idGenerator = new AtomicInteger(1);

    @Autowired
    public WorkerService(WorkerRepository workerRepository,
                         UserRepository userRepository,
                         WorkerMapper workerMapper) {
        this.workerRepository = workerRepository;
        this.userRepository = userRepository;
        this.workerMapper = workerMapper;
        seedInitialData();
    }

    public WorkerService() {
        this(null, null, null);
    }

    // ==========================================
    // Métodos del laboratorio (JPA + MapStruct)
    // ==========================================

    // Crear: el perfil de trabajador se asocia a un usuario que ya existe
    public WorkerModel crear(WorkerModel model) {
        if (model.getUser() == null || model.getUser().getId() <= 0) {
            throw new IllegalArgumentException("El trabajador debe estar asociado a un usuario existente");
        }
        if (userRepository == null || workerRepository == null || workerMapper == null) {
            UserModel userModel = usersByEmail.values().stream()
                    .filter(u -> u.getId() == model.getUser().getId())
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + model.getUser().getId()));
            if (workers.values().stream().anyMatch(w -> w.getUser() != null && w.getUser().getId() == userModel.getId())) {
                throw new IllegalStateException("El usuario ya tiene un perfil de trabajador");
            }
            int newId = idGenerator.getAndIncrement();
            model.setId(newId);
            model.setUser(userModel);
            model.setState(true);
            model.setFinishedJobs(0);
            workers.put(newId, model);
            return model;
        }
        User user = userRepository.findById((long) model.getUser().getId())
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + model.getUser().getId()));
        if (workerRepository.findByUserId(user.getId()).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene un perfil de trabajador");
        }
        Worker entity = workerMapper.toEntity(model);
        entity.setId(null);
        entity.setUser(user); // usuario cargado de la BD, no el que llega en el modelo
        entity.setState(true); // regla de negocio: todo trabajador nace Activo
        entity.setFinishedJobs(0);
        return workerMapper.toModel(workerRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public WorkerModel buscarPorId(Long id) {
        if (workerRepository == null || workerMapper == null) {
            WorkerModel model = workers.get(id.intValue());
            if (model == null) {
                throw new NoSuchElementException("Trabajador no encontrado: " + id);
            }
            return model;
        }
        return workerMapper.toModel(buscarEntidad(id));
    }

    // Solo trabajadores activos: los inactivos no aparecen en las busquedas
    @Transactional(readOnly = true)
    public List<WorkerModel> listarActivos() {
        if (workerRepository == null || workerMapper == null) {
            return workers.values().stream().filter(WorkerModel::isState).toList();
        }
        return workerRepository.findByStateTrue().stream().map(workerMapper::toModel).toList();
    }

    public WorkerModel actualizar(Long id, WorkerModel model) {
        if (workerRepository == null || workerMapper == null) {
            WorkerModel existing = workers.get(id.intValue());
            if (existing == null) {
                throw new NoSuchElementException("Trabajador no encontrado: " + id);
            }
            if (!existing.isState()) {
                throw new IllegalStateException("No se puede modificar un trabajador inactivo");
            }
            if (model.getRate() > 0) {
                existing.setRate(model.getRate());
            }
            if (model.getSalary() > 0) {
                existing.setSalary(model.getSalary());
            }
            return existing;
        }
        Worker entity = buscarEntidad(id);
        if (Boolean.FALSE.equals(entity.getState())) {
            throw new IllegalStateException("No se puede modificar un trabajador inactivo");
        }
        if (model.getRate() > 0) {
            entity.setRate(model.getRate());
        }
        if (model.getSalary() > 0) {
            entity.setSalary(model.getSalary());
        }
        return workerMapper.toModel(workerRepository.save(entity));
    }

    // "Eliminar" = inactivar. Nunca se borra la fila ni se toca el User asociado
    public void inactivar(Long id) {
        if (workerRepository == null) {
            WorkerModel model = workers.get(id.intValue());
            if (model != null) {
                model.setState(false);
            }
            return;
        }
        Worker entity = buscarEntidad(id);
        entity.setState(false);
        workerRepository.save(entity);
    }

    private Worker buscarEntidad(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Trabajador no encontrado: " + id));
    }

    // ==========================================
    // Métodos para Controller, DTOs y compatibilidad
    // ==========================================

    @Transactional(readOnly = true)
    public List<WorkerResponseDTO> findAll(String zona, boolean onlyActive) {
        log.debug("Buscando trabajadores con zona: {} y soloActivos: {}", zona, onlyActive);
        List<WorkerModel> list;
        if (workerRepository != null && workerMapper != null) {
            List<Worker> entities = onlyActive ? workerRepository.findByStateTrue() : workerRepository.findAll();
            list = entities.stream().map(workerMapper::toModel).toList();
        } else {
            list = workers.values().stream()
                    .filter(w -> !onlyActive || w.isState())
                    .toList();
        }

        if (zona != null && !zona.isBlank()) {
            list = list.stream()
                    .filter(w -> w.getWorkZone() != null &&
                            w.getWorkZone().getLocalidad() != null &&
                            w.getWorkZone().getLocalidad().toLowerCase().contains(zona.toLowerCase()))
                    .toList();
        }

        log.info("Total trabajadores encontrados: {}", list.size());
        return list.stream().map(this::mapToResponseDTO).toList();
    }

    @Transactional(readOnly = true)
    public WorkerResponseDTO findById(int id) {
        log.debug("Buscando trabajador con id: {}", id);
        if (workerRepository != null && workerMapper != null) {
            Worker entity = workerRepository.findById((long) id)
                    .orElseThrow(() -> new ResourceNotFoundException("Trabajador con id " + id + " no encontrado."));
            return mapToResponseDTO(workerMapper.toModel(entity));
        }
        WorkerModel worker = workers.get(id);
        if (worker == null) {
            throw new ResourceNotFoundException("Trabajador con id " + id + " no encontrado.");
        }
        return mapToResponseDTO(worker);
    }

    public WorkerResponseDTO create(WorkerRegistrationDTO dto) {
        log.debug("Registrando trabajador con correo: {}", dto != null ? dto.getCorreo() : null);
        validateRegistrationFields(dto);

        String email = dto.getCorreo().trim().toLowerCase();

        if (userRepository != null && workerRepository != null && workerMapper != null) {
            User user = userRepository.findByCorreo(email).orElseGet(() -> {
                User newUser = new User(
                        dto.getNombre(),
                        dto.getFoto() != null ? dto.getFoto().toString() : null,
                        email,
                        String.valueOf(dto.getTelefono())
                );
                return userRepository.save(newUser);
            });

            if (workerRepository.findByUserId(user.getId()).isPresent()) {
                throw new IllegalStateException("El usuario ya tiene un perfil de trabajador");
            }

            Worker entity = new Worker();
            entity.setUser(user);
            entity.setRate(dto.getRate());
            entity.setSalary(dto.getSalary());
            entity.setState(true);
            entity.setFinishedJobs(0);
            Worker saved = workerRepository.save(entity);

            WorkerModel model = workerMapper.toModel(saved);
            model.setPrincipalJob(dto.getPrincipalJob());
            model.setWorkZone(dto.getWorkZone());
            model.setSecondaryJobs(dto.getSecondaryJobs());
            model.setDisponibilities(dto.getDisponibilities());

            return mapToResponseDTO(model);
        }

        UserModel existingUser = usersByEmail.get(email);
        UserModel user;
        WorkerModel worker;

        if (existingUser != null) {
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
                worker = new WorkerModel();
                worker.setId(idGenerator.getAndIncrement());
                worker.setUser(user);
                user.setWorker(worker);
            }
        } else {
            int newUserId = idGenerator.getAndIncrement();
            user = new UserModel(newUserId, dto.getNombre(), email, dto.getTelefono(), dto.getPasswd(), dto.getFoto());
            usersByEmail.put(email, user);

            int newWorkerId = idGenerator.getAndIncrement();
            worker = new WorkerModel();
            worker.setId(newWorkerId);
            worker.setUser(user);
            user.setWorker(worker);
        }

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
        worker.setState(true);

        workers.put(worker.getId(), worker);
        log.info("Trabajador creado exitosamente con id: {}", worker.getId());
        return mapToResponseDTO(worker);
    }

    public WorkerResponseDTO update(int id, WorkerUpdateDTO dto) {
        log.debug("Actualizando trabajador con id: {}", id);
        if (workerRepository != null && workerMapper != null) {
            Worker entity = workerRepository.findById((long) id)
                    .orElseThrow(() -> new ResourceNotFoundException("Trabajador con id " + id + " no encontrado."));
            if (Boolean.FALSE.equals(entity.getState())) {
                throw new InactiveWorkerException("No se puede actualizar la información de un trabajador con estado Inactivo.");
            }
            if (dto.getRate() != null) {
                entity.setRate(dto.getRate());
            }
            if (dto.getSalary() != null) {
                entity.setSalary(dto.getSalary());
            }
            if (dto.getNombre() != null && !dto.getNombre().isBlank() && entity.getUser() != null) {
                entity.getUser().setNombre(dto.getNombre());
            }
            if (dto.getFoto() != null && entity.getUser() != null) {
                entity.getUser().setFoto(dto.getFoto().toString());
            }
            Worker saved = workerRepository.save(entity);
            WorkerModel model = workerMapper.toModel(saved);
            if (dto.getPrincipalJob() != null) model.setPrincipalJob(dto.getPrincipalJob());
            if (dto.getSecondaryJobs() != null) model.setSecondaryJobs(dto.getSecondaryJobs());
            if (dto.getWorkZone() != null) model.setWorkZone(dto.getWorkZone());
            if (dto.getDisponibilities() != null) model.setDisponibilities(dto.getDisponibilities());
            return mapToResponseDTO(model);
        }

        WorkerModel worker = workers.get(id);
        if (worker == null) {
            throw new ResourceNotFoundException("Trabajador con id " + id + " no encontrado.");
        }
        if (!worker.isState()) {
            throw new InactiveWorkerException("No se puede actualizar la información de un trabajador con estado Inactivo.");
        }
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

    public WorkerResponseDTO inactivate(int id) {
        log.debug("Inactivando trabajador con id: {}", id);
        if (workerRepository != null && workerMapper != null) {
            Worker entity = workerRepository.findById((long) id)
                    .orElseThrow(() -> new ResourceNotFoundException("Trabajador con id " + id + " no encontrado."));
            entity.setState(false);
            Worker saved = workerRepository.save(entity);
            return mapToResponseDTO(workerMapper.toModel(saved));
        }
        WorkerModel worker = workers.get(id);
        if (worker == null) {
            throw new ResourceNotFoundException("Trabajador con id " + id + " no encontrado.");
        }
        worker.setState(false);
        log.info("Trabajador inactivado exitosamente con id: {}", id);
        return mapToResponseDTO(worker);
    }

    @Transactional(readOnly = true)
    public Optional<UserModel> findUserByEmail(String email) {
        log.debug("Buscando usuario con correo: {}", email);
        if (email == null) {
            return Optional.empty();
        }
        String cleanEmail = email.trim().toLowerCase();
        UserModel user = usersByEmail.get(cleanEmail);
        if (user != null) {
            return Optional.of(user);
        }
        if (userRepository != null) {
            return userRepository.findByCorreo(cleanEmail)
                    .or(() -> userRepository.findByCorreo(email))
                    .map(u -> new UserModel(
                            u.getId() != null ? u.getId().intValue() : 0,
                            u.getNombre(),
                            u.getCorreo(),
                            u.getTelefono() != null ? Integer.parseInt(u.getTelefono().replaceAll("\\D", "")) : 0,
                            null,
                            null
                    ));
        }
        return Optional.empty();
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

    public WorkerResponseDTO mapToResponseDTO(WorkerModel worker) {
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

    private void seedInitialData() {
        int idU1 = idGenerator.getAndIncrement();
        UserModel u1 = new UserModel(idU1, "Carlos Perez", "carlos.perez@ejemplo.com", 300123456, "clave123", null);
        usersByEmail.put(u1.getCorreo().toLowerCase(), u1);

        int idW1 = 1;
        WorkerModel w1 = new WorkerModel(idW1, u1, 50000, new Job("Plomero", true),
                new WorkZone(1, "Bogota", "Usaquen", "Cedritos"));
        w1.setState(true);
        w1.setFinishedJobs(12);
        w1.getDisponibilities().add(new Disponibility(true));
        u1.setWorker(w1);
        workers.put(idW1, w1);

        int idU2 = idGenerator.getAndIncrement();
        UserModel u2 = new UserModel(idU2, "Ana Gomez", "ana.gomez@ejemplo.com", 310987654, "segura456", null);
        usersByEmail.put(u2.getCorreo().toLowerCase(), u2);

        int idW2 = 2;
        WorkerModel w2 = new WorkerModel(idW2, u2, 65000, new Job("Electricista", true),
                new WorkZone(2, "Bogota", "Chapinero", "Antiguo Country"));
        w2.setState(true);
        w2.setFinishedJobs(25);
        w2.getDisponibilities().add(new Disponibility(true));
        u2.setWorker(w2);
        workers.put(idW2, w2);

        int idU3 = idGenerator.getAndIncrement();
        UserModel u3 = new UserModel(idU3, "Pedro Ramirez", "pedro.ramirez@ejemplo.com", 320112233, "inactivo789", null);
        usersByEmail.put(u3.getCorreo().toLowerCase(), u3);

        int idW3 = 3;
        WorkerModel w3 = new WorkerModel(idW3, u3, 40000, new Job("Carpintero", false),
                new WorkZone(3, "Bogota", "Suba", "La Colina"));
        w3.setState(false);
        w3.setFinishedJobs(5);
        u3.setWorker(w3);
        workers.put(idW3, w3);

        int idU4 = idGenerator.getAndIncrement();
        UserModel u4 = new UserModel(idU4, "Maria Lopez", "maria.lopez@ejemplo.com", 315443322, "contratante2026", null);
        usersByEmail.put(u4.getCorreo().toLowerCase(), u4);
    }
}