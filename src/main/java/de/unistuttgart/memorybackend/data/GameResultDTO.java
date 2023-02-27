package de.unistuttgart.memorybackend.data;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameResultDTO {

    @Nullable
    UUID id;

    boolean isCompleted;
}
