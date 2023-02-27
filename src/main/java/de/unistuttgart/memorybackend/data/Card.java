package de.unistuttgart.memorybackend.data;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

    UUID id;

    String content;

    CardType type;

    public Card(final String content, final CardType type) {
        this.content = content;
        this.type = type;
    }
}
