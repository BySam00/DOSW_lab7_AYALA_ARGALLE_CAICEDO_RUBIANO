package edu.eci.dosw.oficioya.controller;

import edu.eci.dosw.oficioya.model.UserModel;
import edu.eci.dosw.oficioya.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Operaciones relacionadas con usuarios")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Crear o guardar un usuario", description = "Registra un nuevo usuario en la base de datos.")
    public ResponseEntity<UserModel> guardar(@RequestBody UserModel user) {
        UserModel nuevoUsuario = userService.crear(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoUsuario);
    }

    @GetMapping
    @Operation(summary = "Listar usuarios", description = "Obtiene la lista completa de usuarios persistidos.")
    public ResponseEntity<List<UserModel>> listar() {
        return ResponseEntity.ok(userService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por ID", description = "Obtiene un usuario dado su identificador único.")
    public ResponseEntity<UserModel> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(userService.buscarPorId(id));
    }

    @GetMapping("/correo/{correo}")
    @Operation(summary = "Buscar usuario por correo", description = "Obtiene un usuario dado su correo electrónico.")
    public ResponseEntity<UserModel> buscarPorCorreo(@PathVariable String correo) {
        return ResponseEntity.ok(userService.buscarPorCorreo(correo));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar usuario", description = "Actualiza los datos personales de un usuario existente.")
    public ResponseEntity<UserModel> actualizar(@PathVariable Long id, @RequestBody UserModel user) {
        return ResponseEntity.ok(userService.actualizar(id, user));
    }
}
