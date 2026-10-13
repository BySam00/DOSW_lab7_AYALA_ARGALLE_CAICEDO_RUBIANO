package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.entity.Credential;
import edu.eci.dosw.oficioya.entity.User;
import edu.eci.dosw.oficioya.mapper.CredentialMapper;
import edu.eci.dosw.oficioya.model.CredentialModel;
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
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialRepository credentialRepository,
                             UserRepository userRepository,
                             PasswordEncoder passwordEncoder,
                             CredentialMapper credentialMapper) {
        this.credentialRepository = credentialRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.credentialMapper = credentialMapper;
    }

    public CredentialModel registrar(Long userId, String rawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + userId));
        if (credentialRepository.findByUserId(userId).isPresent()) {
            throw new IllegalStateException("El usuario ya tiene credenciales registradas");
        }
        Credential credential = new Credential(passwordEncoder.encode(rawPassword), null);
        credential.setUser(user);
        return credentialMapper.toModel(credentialRepository.save(credential));
    }

    @Transactional(readOnly = true)
    public boolean validarContrasena(Long userId, String rawPassword) {
        return credentialRepository.findByUserId(userId)
                .map(cred -> passwordEncoder.matches(rawPassword, cred.getPasswordHash()))
                .orElse(false);
    }

    @Transactional(readOnly = true)
    public Optional<CredentialModel> buscarPorUsuarioId(Long userId) {
        return credentialRepository.findByUserId(userId).map(credentialMapper::toModel);
    }
}
