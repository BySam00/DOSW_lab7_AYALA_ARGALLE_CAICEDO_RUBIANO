package edu.eci.dosw.oficioya.controller;

import edu.eci.dosw.oficioya.dto.WorkerRegistrationDTO;
import edu.eci.dosw.oficioya.dto.WorkerResponseDTO;
import edu.eci.dosw.oficioya.dto.WorkerUpdateDTO;
import edu.eci.dosw.oficioya.exception.InactiveWorkerException;
import edu.eci.dosw.oficioya.exception.ResourceNotFoundException;
import edu.eci.dosw.oficioya.model.Job;
import edu.eci.dosw.oficioya.model.WorkZone;
import edu.eci.dosw.oficioya.service.WorkerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WorkerControllerTest {

    private WorkerController workerController;
    private WorkerService workerService;

    @BeforeEach
    void setUp() {
        workerService = new WorkerService();
        workerController = new WorkerController(workerService);
    }

    @Test
    void testGetAllWorkers() {
        ResponseEntity<List<WorkerResponseDTO>> response = workerController.getAllWorkers(null, false);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void testGetWorkerByIdSuccess() {
        ResponseEntity<WorkerResponseDTO> response = workerController.getWorkerById(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
        assertEquals("Carlos Perez", response.getBody().getNombre());
    }

    @Test
    void testGetWorkerByIdNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> workerController.getWorkerById(9999));
    }

    @Test
    void testCreateWorkerSuccess() {
        WorkerRegistrationDTO dto = new WorkerRegistrationDTO(
                "Diana Pinzon",
                "diana.pinzon@ejemplo.com",
                319556677,
                new Job("Jardinera", false),
                "jardin2026"
        );
        dto.setRate(42000);
        dto.setWorkZone(new WorkZone(5, "Bogota", "Teusaquillo", "Park Way"));

        ResponseEntity<WorkerResponseDTO> response = workerController.createWorker(dto);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Diana Pinzon", response.getBody().getNombre());
        assertTrue(response.getBody().isState());
        assertEquals(42000, response.getBody().getRate());
    }

    @Test
    void testUpdateWorkerSuccess() {
        WorkerUpdateDTO updateDTO = new WorkerUpdateDTO();
        updateDTO.setRate(85000);
        updateDTO.setNombre("Carlos Perez Actualizado");

        ResponseEntity<WorkerResponseDTO> response = workerController.updateWorker(1, updateDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(85000, response.getBody().getRate());
        assertEquals("Carlos Perez Actualizado", response.getBody().getNombre());
    }

    @Test
    void testUpdateWorkerInactiveFails() {
        // Trabajador 3 es inactivo
        WorkerUpdateDTO updateDTO = new WorkerUpdateDTO();
        updateDTO.setRate(99000);

        assertThrows(InactiveWorkerException.class, () -> workerController.updateWorker(3, updateDTO));
    }

    @Test
    void testDeleteWorkerLogicalInactivation() {
        ResponseEntity<WorkerResponseDTO> response = workerController.deleteWorker(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isState());

        // Después de inactivado, cualquier update debe fallar
        WorkerUpdateDTO updateDTO = new WorkerUpdateDTO();
        updateDTO.setRate(50000);
        assertThrows(InactiveWorkerException.class, () -> workerController.updateWorker(1, updateDTO));
    }

    @Test
    void testPatchInactivateWorker() {
        ResponseEntity<WorkerResponseDTO> response = workerController.inactivateWorker(2);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isState());
    }
}
