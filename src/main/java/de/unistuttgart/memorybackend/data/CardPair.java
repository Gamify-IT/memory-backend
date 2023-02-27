package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardPair {

    @Id
    UUID id;

    @ManyToOne
    Card card1;
    @ManyToOne
    Card card2;

    public CardPair(final Card card1, final Card card2) {
            this.card1 = card1;
            this.card2 = card2;
        }
}
