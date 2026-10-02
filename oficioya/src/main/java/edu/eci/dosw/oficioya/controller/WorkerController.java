package edu.eci.dosw.oficioya.controller;

import edu.eci.dosw.oficioya.dto.WorkerRegistrationDTO;
import edu.eci.dosw.oficioya.dto.WorkerResponseDTO;
import edu.eci.dosw.oficioya.dto.WorkerUpdateDTO;
import edu.eci.dosw.oficioya.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/workers")
@Tag(name = "Workers", description = "Operaciones relacionadas con trabajadores")
public class WorkerController {

    private final WorkerService workerService;

    @Autowired
    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    /**
     * Listar trabajadores (Read varios).
     * Permite filtrar por zona y por defecto solo muestra trabajadores activos.
     */
    @GetMapping
    @Operation(summary = "Listar trabajadores", description = "Obtiene una lista de trabajadores. Permite filtrar por zona y por defecto solo muestra trabajadores activos.")
    public ResponseEntity<List<WorkerResponseDTO>> getAllWorkers(
            @Parameter(description = "Nombre de la zona de trabajo para filtrar", example = "Norte") 
            @RequestParam(required = false) String zona,

            @Parameter(description = "Si se envía en true, la respuesta incluirá también a los trabajadores inactivos")
            @RequestParam(required = false, defaultValue = "false") boolean incluirInactivos) {
        List<WorkerResponseDTO> workers = workerService.findAll(zona, !incluirInactivos);
        return ResponseEntity.ok(workers);
    }

    /**
     * Consultar perfil de un trabajador específico (Read individual).
     */
    @GetMapping("/{id}")
    @Operation(summary = "Consultar perfil de un trabajador", description = "Obtiene la información detallada de un trabajador específico utilizando su número de ID.")
    public ResponseEntity<WorkerResponseDTO> getWorkerById(
        @Parameter(description = "El ID único del trabajador que se desea buscar", example = "1")
        @PathVariable int id
    ) {
        WorkerResponseDTO worker = workerService.findById(id);
        return ResponseEntity.ok(worker);
    }

    /**
     * Registrar un nuevo trabajador (Create).
     * Los trabajadores se crean por defecto con estado Activo.
     * Retorna HTTP 201 Created.
     */
    @PostMapping
    @Operation(summary = "Registrar un nuevo trabajador", description = "Crea un nuevo trabajador en el sistema. Por defecto, el trabajador se inicializa con estado Activo."
    )
    public ResponseEntity<WorkerResponseDTO> createWorker(@RequestBody WorkerRegistrationDTO workerDTO) {
        WorkerResponseDTO created = workerService.create(workerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
     
    }

    /*
     * Actualizar datos del trabajador (Update).
     * 
     * 
     * Si el trabajador está inactivo, se rechaza la actualización según la regla de
     * negocio.
     */
    @PutMapping("/{id}")
    @Operation(summary ="Actualizar datos del trabajador", description ="actualiza los datos de un trabajor con base en su id, si esta inactivo el trabajador no se hara la actualizacions")
    public ResponseEntity<WorkerResponseDTO> updateWorker(
        @Parameter(description = "El ID único del trabajador que se desea buscar", example = "1")
        @PathVariable int id,
        
        @RequestBody WorkerUpdateDTO updateDTO) {
        WorkerResponseDTO updated = workerService.update(id, updateDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Inactivación lógica mediante DELETE (Delete -> Inactivar).
     * No existe borrado físico; cambia el estado a Inactivo sin tocar el User.
     */
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Inactivar un trabajador", 
        description = "Cinactiva a algun trbajador dado su id."
    )
    public ResponseEntity<WorkerResponseDTO> deleteWorker(
        @Parameter(description = "El ID único del trabajador que se desea buscar", example = "1")
        @PathVariable int id) {
        WorkerResponseDTO inactivated = workerService.inactivate(id);
        return ResponseEntity.ok(inactivated);
    }

    /**
     * Endpoint semántico alternativo para inactivación lógica directa.
     */
    @PatchMapping("/{id}/inactivar")
    public ResponseEntity<WorkerResponseDTO> inactivateWorker(@PathVariable int id) {
        WorkerResponseDTO inactivated = workerService.inactivate(id);
        return ResponseEntity.ok(inactivated);
    }
}
