package de.unistuttgart.memorybackend.data;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class GameResultDTO {

    private Boolean isFinished;

    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    @NotNull(message = "playerId cannot be null")
    private String playerId;

    private int rewards;

    public GameResultDTO(final Boolean isFinished, final UUID configurationAsUUID, final String playerId, final int rewards) {
        this.isFinished = isFinished;
        this.configurationAsUUID = configurationAsUUID;
        this.playerId = playerId;
        this.rewards = rewards;
    }


}
