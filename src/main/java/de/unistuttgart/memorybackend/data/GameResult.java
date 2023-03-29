package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class GameResult {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    boolean isCompleted;

    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    @NotNull(message = "playerId cannot be null")
    private String playerId;

    public GameResult(final boolean isCompleted, final UUID configurationAsUUID, final String playerId) {
        this.isCompleted = isCompleted;
        this.configurationAsUUID = configurationAsUUID;
        this.playerId = playerId;
    }
}
