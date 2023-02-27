package de.unistuttgart.memorybackend.data;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardPair {
    
    UUID id;
    
    Card card1;
    Card card2;

    public CardPair(final Card card1, final Card card2) {
            this.card1 = card1;
            this.card2 = card2;
        }
}
