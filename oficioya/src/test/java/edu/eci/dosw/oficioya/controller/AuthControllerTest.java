package edu.eci.dosw.oficioya.controller;

import edu.eci.dosw.oficioya.dto.LoginRequestDTO;
import edu.eci.dosw.oficioya.dto.LoginResponseDTO;
import edu.eci.dosw.oficioya.service.AuthService;
import edu.eci.dosw.oficioya.service.WorkerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController authController;

    @BeforeEach
    void setUp() {
        WorkerService workerService = new WorkerService();
        AuthService authService = new AuthService(workerService);
        authController = new AuthController(authService);
    }

    @Test
    void testLoginSuccess() {
        LoginRequestDTO request = new LoginRequestDTO("carlos.perez@ejemplo.com", "clave123");
        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isAuthenticated());
        assertEquals("carlos.perez@ejemplo.com", response.getBody().getCorreo());
    }

    @Test
    void testLoginUnauthorizedWrongPassword() {
        LoginRequestDTO request = new LoginRequestDTO("carlos.perez@ejemplo.com", "claveIncorrecta");
        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAuthenticated());
    }

    @Test
    void testLoginUnauthorizedUnknownUser() {
        LoginRequestDTO request = new LoginRequestDTO("noexiste@ejemplo.com", "password123");
        ResponseEntity<LoginResponseDTO> response = authController.login(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertFalse(response.getBody().isAuthenticated());
    }
}
