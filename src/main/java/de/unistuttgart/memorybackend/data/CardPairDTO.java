package de.unistuttgart.memorybackend.data;

import java.util.Objects;
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

    public boolean equalsContent(final CardPairDTO other) {
        if (this == other) return true;
        if (other == null) return false;
        return (
            Objects.equals(card1.getContent(), other.card1.getContent()) &&
            Objects.equals(card1.getType(), other.card1.getType()) &&
            Objects.equals(card2.getContent(), other.card2.getContent()) &&
            Objects.equals(card2.getType(), other.card2.getType())
        );
    }
}
