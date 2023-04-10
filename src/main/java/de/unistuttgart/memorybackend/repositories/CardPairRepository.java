package de.unistuttgart.memorybackend.repositories;

import de.unistuttgart.memorybackend.data.CardPair;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardPairRepository extends JpaRepository<CardPair, UUID> {}
