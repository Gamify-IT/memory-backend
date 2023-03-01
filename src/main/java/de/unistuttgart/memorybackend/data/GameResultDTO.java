package de.unistuttgart.memorybackend.data;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GameResultDTO {

    @Nullable
    UUID id;

    boolean isCompleted;
}
