package edu.eci.dosw.oficioya.repository;

import edu.eci.dosw.oficioya.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CredentialRepository extends JpaRepository<Credential, Long> {

    Optional<Credential> findByUserId(Long userId);
}
