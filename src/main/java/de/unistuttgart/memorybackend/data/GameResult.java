package de.unistuttgart.memorybackend.data;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GameResult {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    boolean isCompleted;

    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    @NotNull(message = "playerId cannot be null")
    private String playerId;

    public GameResult(
            final boolean isCompleted,
            final UUID configurationAsUUID,
            final String playerId
    ) {
        this.isCompleted = isCompleted;
        this.configurationAsUUID = configurationAsUUID;
        this.playerId = playerId;
    }
}
