package edu.eci.dosw.oficioya.service;

import edu.eci.dosw.oficioya.entity.RoleType;
import edu.eci.dosw.oficioya.entity.User;
import edu.eci.dosw.oficioya.mapper.UserMapper;
import edu.eci.dosw.oficioya.model.UserModel;
import edu.eci.dosw.oficioya.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserModel crear(UserModel model) {
        if (userRepository.existsByCorreo(model.getCorreo())) {
            throw new IllegalStateException("Ya existe un usuario con el correo " + model.getCorreo());
        }
        User entity = userMapper.toEntity(model);
        entity.setId(null); // un id enviado por el cliente nunca debe actualizar una fila existente
        entity.setState(true);
        entity.setRole(RoleType.USER); // el rol no se toma del cliente
        return userMapper.toModel(userRepository.save(entity));
    }

    @Transactional(readOnly = true)
    public UserModel buscarPorId(Long id) {
        return userMapper.toModel(buscarEntidad(id));
    }

    @Transactional(readOnly = true)
    public UserModel buscarPorCorreo(String correo) {
        return userRepository.findByCorreo(correo)
                .map(userMapper::toModel)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + correo));
    }

    @Transactional(readOnly = true)
    public List<UserModel> listar() {
        return userRepository.findAll().stream().map(userMapper::toModel).toList();
    }

    public UserModel actualizar(Long id, UserModel model) {
        User entity = buscarEntidad(id);
        if (model.getNombre() != null) {
            entity.setNombre(model.getNombre());
        }
        if (model.getFoto() != null) {
            entity.setFoto(model.getFoto().toString());
        }
        if (model.getTelefono() > 0) {
            entity.setTelefono(String.valueOf(model.getTelefono()));
        }
        return userMapper.toModel(userRepository.save(entity));
    }

    private User buscarEntidad(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado: " + id));
    }
}