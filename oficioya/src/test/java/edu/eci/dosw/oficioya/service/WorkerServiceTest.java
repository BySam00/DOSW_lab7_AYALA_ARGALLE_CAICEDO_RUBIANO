package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.dto.WorkerRegistrationDTO;
import edu.eci.dosw.oficioya.dto.WorkerResponseDTO;
import edu.eci.dosw.oficioya.dto.WorkerUpdateDTO;
import edu.eci.dosw.oficioya.exception.InactiveWorkerException;
import edu.eci.dosw.oficioya.exception.InvalidDataException;
import edu.eci.dosw.oficioya.exception.ResourceNotFoundException;
import edu.eci.dosw.oficioya.model.Job;
import edu.eci.dosw.oficioya.model.WorkZone;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkerServiceTest {

    private WorkerService workerService;

    @BeforeEach
    void setUp() {
        workerService = new WorkerService();
    }

    @Test
    void testFindAllOnlyActiveByDefault() {
        List<WorkerResponseDTO> list = workerService.findAll(null, true);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(list.stream().allMatch(WorkerResponseDTO::isState));
    }

    @Test
    void testFindAllWithZoneFilter() {
        List<WorkerResponseDTO> list = workerService.findAll("Usaquen", true);
        assertNotNull(list);
        assertFalse(list.isEmpty());
        assertTrue(list.get(0).getWorkZone().getLocalidad().toLowerCase().contains("usaquen"));
    }

    @Test
    void testFindByIdSuccess() {
        WorkerResponseDTO worker = workerService.findById(1);
        assertNotNull(worker);
        assertEquals(1, worker.getId());
        assertEquals("Carlos Perez", worker.getNombre());
    }

    @Test
    void testFindByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> workerService.findById(9999));
    }

    @Test
    void testCreateWorkerSuccess() {
        WorkerRegistrationDTO dto = new WorkerRegistrationDTO(
                "Nuevo Trabajador",
                "nuevo@ejemplo.com",
                311223344,
                new Job("Cerrajero", false),
                "claveSegura123"
        );
        dto.setRate(35000);
        dto.setWorkZone(new WorkZone(4, "Bogota", "Teusaquillo", "Galerias"));

        WorkerResponseDTO created = workerService.create(dto);
        assertNotNull(created);
        assertEquals("Nuevo Trabajador", created.getNombre());
        assertTrue(created.isState(), "El trabajador debe crearse con estado Activo por defecto");
        assertEquals("Cerrajero", created.getPrincipalJob().getDescription());
        assertEquals(35000, created.getRate());
    }

    @Test
    void testCreateWorkerWithExistingContractorUser() {
        // maria.lopez@ejemplo.com ya existe en los seed data como contratante (User sin Worker)
        WorkerRegistrationDTO dto = new WorkerRegistrationDTO(
                "Maria Lopez",
                "maria.lopez@ejemplo.com",
                315443322,
                new Job("Pintora", true),
                "contratante2026"
        );
        dto.setRate(48000);

        WorkerResponseDTO created = workerService.create(dto);
        assertNotNull(created);
        assertEquals("Maria Lopez", created.getNombre());
        assertTrue(created.isState());
        assertEquals("Pintora", created.getPrincipalJob().getDescription());
    }

    @Test
    void testCreateWorkerMissingRequiredFields() {
        // Falta nombre
        WorkerRegistrationDTO d1 = new WorkerRegistrationDTO(null, "test@test.com", 300123, new Job("Job"), "pass");
        assertThrows(InvalidDataException.class, () -> workerService.create(d1));

        // Falta correo
        WorkerRegistrationDTO d2 = new WorkerRegistrationDTO("Name", null, 300123, new Job("Job"), "pass");
        assertThrows(InvalidDataException.class, () -> workerService.create(d2));

        // Falta telefono
        WorkerRegistrationDTO d3 = new WorkerRegistrationDTO("Name", "test@test.com", null, new Job("Job"), "pass");
        assertThrows(InvalidDataException.class, () -> workerService.create(d3));

        // Falta oficio principal
        WorkerRegistrationDTO d4 = new WorkerRegistrationDTO("Name", "test@test.com", 300123, null, "pass");
        assertThrows(InvalidDataException.class, () -> workerService.create(d4));

        // Falta contraseña
        WorkerRegistrationDTO d5 = new WorkerRegistrationDTO("Name", "test@test.com", 300123, new Job("Job"), "");
        assertThrows(InvalidDataException.class, () -> workerService.create(d5));
    }

    @Test
    void testUpdateWorkerSuccessWhenActive() {
        WorkerUpdateDTO updateDTO = new WorkerUpdateDTO();
        updateDTO.setRate(75000);
        updateDTO.setNombre("Carlos Perez Actualizado");

        WorkerResponseDTO updated = workerService.update(1, updateDTO);
        assertNotNull(updated);
        assertEquals(75000, updated.getRate());
        assertEquals("Carlos Perez Actualizado", updated.getNombre());
    }

    @Test
    void testUpdateWorkerFailsWhenInactive() {
        // Worker 3 fue creado inactivo en seedData
        WorkerUpdateDTO updateDTO = new WorkerUpdateDTO();
        updateDTO.setRate(55000);

        assertThrows(InactiveWorkerException.class, () -> workerService.update(3, updateDTO));
    }

    @Test
    void testInactivateWorker() {
        WorkerResponseDTO inactivated = workerService.inactivate(1);
        assertNotNull(inactivated);
        assertFalse(inactivated.isState(), "El estado debe cambiar a inactivo (false)");

        // Ahora no debe permitir actualizarlo
        WorkerUpdateDTO updateDTO = new WorkerUpdateDTO();
        updateDTO.setRate(99000);
        assertThrows(InactiveWorkerException.class, () -> workerService.update(1, updateDTO));
    }
}
