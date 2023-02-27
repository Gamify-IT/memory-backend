package de.unistuttgart.memorybackend.data;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GameResultDTO {

    @Nullable
    UUID id;

    boolean isCompleted;
}
