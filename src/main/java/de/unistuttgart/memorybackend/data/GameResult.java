package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameResult {

    @Id
    UUID id;

    boolean isCompleted;
}
