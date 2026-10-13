package edu.eci.dosw.oficioya.controller;

import edu.eci.dosw.oficioya.model.CredentialModel;
import edu.eci.dosw.oficioya.service.CredentialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/credentials")
@Tag(name = "Credentials", description = "Operaciones de seguridad y credenciales de usuario")
public class CredentialController {

    private final CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/registrar")
    @Operation(summary = "Registrar credencial", description = "Registra una contraseña cifrada para un usuario existente.")
    public ResponseEntity<CredentialModel> registrar(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String password = request.get("password").toString();
        CredentialModel model = credentialService.registrar(userId, password);
        return ResponseEntity.status(HttpStatus.CREATED).body(model);
    }

    @PostMapping("/validar")
    @Operation(summary = "Validar contraseña", description = "Valida si la contraseña coincide con el hash almacenado para el usuario.")
    public ResponseEntity<Boolean> validar(@RequestBody Map<String, Object> request) {
        Long userId = Long.valueOf(request.get("userId").toString());
        String password = request.get("password").toString();
        boolean valid = credentialService.validarContrasena(userId, password);
        return ResponseEntity.ok(valid);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Consultar credencial por usuario", description = "Obtiene los metadatos de credencial asociados a un usuario.")
    public ResponseEntity<CredentialModel> buscarPorUsuarioId(@PathVariable Long userId) {
        return credentialService.buscarPorUsuarioId(userId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
