package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.dto.LoginRequestDTO;
import edu.eci.dosw.oficioya.dto.LoginResponseDTO;
import edu.eci.dosw.oficioya.exception.InvalidDataException;
import edu.eci.dosw.oficioya.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    private final WorkerService workerService;

    @Autowired
    public AuthService(WorkerService workerService) {
        this.workerService = workerService;
    }

    /**
     * Autenticación por método POST comparando correo y contraseña.
     */
    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        if (request == null) {
            throw new InvalidDataException("La solicitud de autenticación no puede ser nula.");
        }
        if (request.getCorreo() == null || request.getCorreo().isBlank()) {
            throw new InvalidDataException("El correo es obligatorio.");
        }
        if (request.getPasswd() == null || request.getPasswd().isBlank()) {
            throw new InvalidDataException("La contraseña es obligatoria.");
        }

        Optional<User> userOpt = workerService.findUserByEmail(request.getCorreo());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPasswd() != null && user.getPasswd().equals(request.getPasswd())) {
                return LoginResponseDTO.success(user.getId(), user.getNombre(), user.getCorreo());
            }
        }

        return LoginResponseDTO.failure("Credenciales invalidas: correo o contraseña incorrectos.");
    }
}
