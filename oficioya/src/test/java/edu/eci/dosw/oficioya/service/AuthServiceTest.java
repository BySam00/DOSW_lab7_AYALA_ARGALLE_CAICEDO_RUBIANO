package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.dto.LoginRequestDTO;
import edu.eci.dosw.oficioya.dto.LoginResponseDTO;
import edu.eci.dosw.oficioya.exception.InvalidDataException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    private AuthService authService;

    @BeforeEach
    void setUp() {
        WorkerService workerService = new WorkerService();
        authService = new AuthService(workerService);
    }

    @Test
    void testAuthenticateSuccess() {
        LoginRequestDTO request = new LoginRequestDTO("carlos.perez@ejemplo.com", "clave123");
        LoginResponseDTO response = authService.authenticate(request);

        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertEquals("carlos.perez@ejemplo.com", response.getCorreo());
        assertEquals("Carlos Perez", response.getNombre());
    }

    @Test
    void testAuthenticateInvalidPassword() {
        LoginRequestDTO request = new LoginRequestDTO("carlos.perez@ejemplo.com", "claveErronea");
        LoginResponseDTO response = authService.authenticate(request);

        assertNotNull(response);
        assertFalse(response.isAuthenticated());
    }

    @Test
    void testAuthenticateNonExistentUser() {
        LoginRequestDTO request = new LoginRequestDTO("noexiste@ejemplo.com", "clave123");
        LoginResponseDTO response = authService.authenticate(request);

        assertNotNull(response);
        assertFalse(response.isAuthenticated());
    }

    @Test
    void testAuthenticateMissingData() {
        assertThrows(InvalidDataException.class, () -> authService.authenticate(new LoginRequestDTO(null, "pass")));
        assertThrows(InvalidDataException.class, () -> authService.authenticate(new LoginRequestDTO("mail@mail.com", null)));
        assertThrows(InvalidDataException.class, () -> authService.authenticate(null));
    }
}
