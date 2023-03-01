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
public class CardPairDTO {

    /**
     * A unique identifier for the game result.
     */
    @Nullable
    private UUID id;

    CardDTO card1;
    CardDTO card2;

    public CardPairDTO(final CardDTO card1, final CardDTO card2) {
        this.card1 = card1;
        this.card2 = card2;
    }
}
