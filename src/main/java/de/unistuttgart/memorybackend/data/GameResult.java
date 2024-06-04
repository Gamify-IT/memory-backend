package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.Date;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@Entity
public class GameResult {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    Boolean isFinished;

    @NotNull(message = "configurationAsUUID cannot be null")
    private UUID configurationAsUUID;

    @NotNull(message = "playerId cannot be null")
    private String playerId;

    @NotNull(message = "playedDay cannot be null")
    @CreationTimestamp
    private Date playedDay = new Date();

    private int rewards;

    public GameResult(
        final Boolean isFinished,
        final UUID configurationAsUUID,
        final String playerId,
        final int rewards
    ) {
        this.isFinished = isFinished;
        this.configurationAsUUID = configurationAsUUID;
        this.playerId = playerId;
        this.rewards = rewards;
    }
}
