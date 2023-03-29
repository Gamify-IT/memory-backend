package de.unistuttgart.memorybackend.data;

import java.util.UUID;
import javax.validation.constraints.NotNull;
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

    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    @NotNull(message = "playerId cannot be null")
    private String playerId;

    public GameResultDTO(final boolean isCompleted, final UUID configurationAsUUID, final String playerId) {
        this.isCompleted = isCompleted;
        this.configurationAsUUID = configurationAsUUID;
        this.playerId = playerId;
    }
}
