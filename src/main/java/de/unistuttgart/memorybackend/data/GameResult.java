package de.unistuttgart.memorybackend.data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    boolean isCompleted;
}
