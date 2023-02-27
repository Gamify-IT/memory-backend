package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

    @Id
    UUID id;

    String content;

    @Enumerated
    CardType type;

    public Card(final String content, final CardType type) {
        this.content = content;
        this.type = type;
    }
}
