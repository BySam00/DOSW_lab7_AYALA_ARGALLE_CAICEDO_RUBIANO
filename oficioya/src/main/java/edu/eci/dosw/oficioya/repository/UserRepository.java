package edu.eci.dosw.oficioya.repository;

import edu.eci.dosw.oficioya.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCorreo(String correo);

    boolean existsByCorreo(String correo);
}