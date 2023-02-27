package de.unistuttgart.memorybackend.data;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

/**
 * The CardDTO.class contains the card related information
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardDTO {

    /**
     * A unique identifier for the game result.
     */
    @Nullable
    private UUID id;

    String content;

    CardType type;

    public CardDTO(final String content, final CardType type) {
        this.content = content;
        this.type = type;
    }
}
