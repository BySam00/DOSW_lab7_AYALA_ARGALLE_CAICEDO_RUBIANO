package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.entity.Credential;
import edu.eci.dosw.oficioya.entity.User;
import edu.eci.dosw.oficioya.repository.CredentialRepository;
import edu.eci.dosw.oficioya.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional
public class CredentialService {

    private final CredentialRepository credentialRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CredentialService(CredentialRepository credentialRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder) {
        this.credentialRepository = credentialRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Credential registrar(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + userId));
        if (credentialRepository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene credenciales registradas");
        }
        Credential credential = new Credential(passwordEncoder.encode(rawPassword), null);
        credential.setUser(user);
        return credentialRepository.save(credential);
    }

    @Transactional(readOnly = true)
    public boolean validarContrasena(Long userId, String rawPassword) {
        return credentialRepository.findByUserId(userId)
                .map(cred -> passwordEncoder.matches(rawPassword, cred.getPasswordHash()))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<Credential> buscarPorUsuarioId(Long userId) {
        return credentialRepository.findByUserId(userId);
    }
}
