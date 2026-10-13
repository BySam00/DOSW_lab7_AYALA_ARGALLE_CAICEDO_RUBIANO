package edu.eci.dosw.oficioya.controller;

import edu.eci.dosw.oficioya.model.CredentialModel;
import edu.eci.dosw.oficioya.service.CredentialService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CredentialControllerTest {

    private CredentialService credentialService;
    private CredentialController credentialController;

    @BeforeEach
    void setUp() {
        credentialService = Mockito.mock(CredentialService.class);
        credentialController = new CredentialController(credentialService);
    }

    @Test
    void testRegistrar() {
        CredentialModel cred = new CredentialModel(1L, "hashedPass", null, null);
        when(credentialService.registrar(1L, "password123")).thenReturn(cred);

        ResponseEntity<CredentialModel> response = credentialController.registrar(Map.of("userId", 1L, "password", "password123"));
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("hashedPass", response.getBody().getPasswordHash());
    }

    @Test
    void testValidar() {
        when(credentialService.validarContrasena(1L, "password123")).thenReturn(true);

        ResponseEntity<Boolean> response = credentialController.validar(Map.of("userId", 1L, "password", "password123"));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
    }

    @Test
    void testBuscarPorUsuarioIdExistente() {
        CredentialModel cred = new CredentialModel(1L, "hashedPass", null, null);
        when(credentialService.buscarPorUsuarioId(1L)).thenReturn(Optional.of(cred));

        ResponseEntity<CredentialModel> response = credentialController.buscarPorUsuarioId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testBuscarPorUsuarioIdNoExistente() {
        when(credentialService.buscarPorUsuarioId(99L)).thenReturn(Optional.empty());

        ResponseEntity<CredentialModel> response = credentialController.buscarPorUsuarioId(99L);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
