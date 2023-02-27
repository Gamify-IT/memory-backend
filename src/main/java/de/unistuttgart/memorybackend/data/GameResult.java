package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameResult {
    @Id
    UUID id;

    boolean isCompleted;
}
