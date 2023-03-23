package de.unistuttgart.memorybackend.repositories;

import de.unistuttgart.memorybackend.data.GameResult;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameResultRepository extends JpaRepository<GameResult, Long> {
    List<GameResult> findByConfigurationAsUUID(UUID configurationId);
}
