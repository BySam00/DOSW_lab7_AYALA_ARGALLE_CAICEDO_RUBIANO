package edu.eci.dosw.oficioya.controller;

import edu.eci.dosw.oficioya.model.UserModel;
import edu.eci.dosw.oficioya.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class UserControllerTest {

    private UserService userService;
    private UserController userController;

    @BeforeEach
    void setUp() {
        userService = Mockito.mock(UserService.class);
        userController = new UserController(userService);
    }

    @Test
    void testGuardar() {
        UserModel user = new UserModel(1, "Juan Perez", "juan@test.com", 300123456, "clave", null);
        when(userService.crear(any(UserModel.class))).thenReturn(user);

        ResponseEntity<UserModel> response = userController.guardar(user);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Juan Perez", response.getBody().getNombre());
    }

    @Test
    void testListar() {
        UserModel user = new UserModel(1, "Juan Perez", "juan@test.com", 300123456, "clave", null);
        when(userService.listar()).thenReturn(List.of(user));

        ResponseEntity<List<UserModel>> response = userController.listar();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testBuscarPorId() {
        UserModel user = new UserModel(1, "Juan Perez", "juan@test.com", 300123456, "clave", null);
        when(userService.buscarPorId(1L)).thenReturn(user);

        ResponseEntity<UserModel> response = userController.buscarPorId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Juan Perez", response.getBody().getNombre());
    }

    @Test
    void testBuscarPorCorreo() {
        UserModel user = new UserModel(1, "Juan Perez", "juan@test.com", 300123456, "clave", null);
        when(userService.buscarPorCorreo("juan@test.com")).thenReturn(user);

        ResponseEntity<UserModel> response = userController.buscarPorCorreo("juan@test.com");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("juan@test.com", response.getBody().getCorreo());
    }

    @Test
    void testActualizar() {
        UserModel user = new UserModel(1, "Juan Modificado", "juan@test.com", 300123456, "clave", null);
        when(userService.actualizar(eq(1L), any(UserModel.class))).thenReturn(user);

        ResponseEntity<UserModel> response = userController.actualizar(1L, user);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Juan Modificado", response.getBody().getNombre());
    }
}
