package edu.eci.dosw.oficioya.repository;

import edu.eci.dosw.oficioya.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WorkerRepository extends JpaRepository<Worker, Long> {

    Optional<Worker> findByUserId(Long userId);

    List<Worker> findByStateTrue();
}