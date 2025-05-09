package de.unistuttgart.memorybackend.data;

import java.util.UUID;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

/**
 * The OverworldResultDTO.class contains all the info that is sent to the Overworld-backend.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Validated
public class OverworldResultDTO {

    /**
     * The name of the minigame. In this case "MEMORY".
     */
    @NotNull(message = "game cannot be null")
    final String game = "MEMORY"; //NOSONAR

    /**
     * The ID of the configuration that was used for the game.
     */
    @NotNull(message = "configurationId cannot be null")
    UUID configurationId;

    /**
     * The score achieved in the game.
     */
    @Min(value = 0, message = "Score cannot be less than " + 0)
    @Max(value = 100, message = "Score cannot be higher than " + 100)
    long score;

    /**
     * The ID of the user that played the game.
     */
    @NotNull(message = "user cannot be null")
    @NotBlank(message = "user cannot be blank")
    String userId;

    /**
     * the rewards gained by the player in this round of memory
     */
    @Min(value = 0, message = "Score cannot be less than " + 0)
    @Max(value = 10, message = "Score cannot be higher than " + 10)
    int rewards;
}
