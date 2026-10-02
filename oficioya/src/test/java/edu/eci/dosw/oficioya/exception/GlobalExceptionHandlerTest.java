package edu.eci.dosw.oficioya.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("No encontrado");
        ResponseEntity<Map<String, Object>> response = handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Recurso no encontrado", response.getBody().get("error"));
        assertEquals("No encontrado", response.getBody().get("message"));
    }

    @Test
    void testHandleInactiveWorker() {
        InactiveWorkerException ex = new InactiveWorkerException("Trabajador inactivo");
        ResponseEntity<Map<String, Object>> response = handler.handleInactiveWorker(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Trabajador Inactivo", response.getBody().get("error"));
    }

    @Test
    void testHandleInvalidData() {
        InvalidDataException ex = new InvalidDataException("Datos faltantes");
        ResponseEntity<Map<String, Object>> response = handler.handleInvalidData(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Datos invalidos", response.getBody().get("error"));
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Error inesperado");
        ResponseEntity<Map<String, Object>> response = handler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error interno del servidor", response.getBody().get("error"));
    }
}
