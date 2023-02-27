package de.unistuttgart.memorybackend.repositories;

import de.unistuttgart.memorybackend.data.Card;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {}