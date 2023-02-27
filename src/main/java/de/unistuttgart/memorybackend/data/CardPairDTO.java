package de.unistuttgart.memorybackend.data;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@FieldDefaults(level = AccessLevel.PRIVATE)
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
