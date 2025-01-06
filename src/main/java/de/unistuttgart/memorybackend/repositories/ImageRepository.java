package de.unistuttgart.memorybackend.repositories;

import de.unistuttgart.memorybackend.data.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<Image, UUID> {
    Optional<Image> findByUUID(UUID uuid);
}