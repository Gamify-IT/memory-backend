package de.unistuttgart.memorybackend.data;

import jakarta.persistence.*;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class CardPair {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @ManyToOne(cascade = CascadeType.ALL)
    Card card1;

    @ManyToOne(cascade = CascadeType.ALL)
    Card card2;

    public CardPair(final Card card1, final Card card2) {
        this.card1 = card1;
        this.card2 = card2;
    }

    @Override
    public CardPair clone() {
        return new CardPair(card1, card2);
    }
}
