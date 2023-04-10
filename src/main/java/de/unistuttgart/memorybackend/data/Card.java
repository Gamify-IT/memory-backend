package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Card {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    String content;

    @Enumerated
    CardType type;

    public Card(final String content, final CardType type) {
        this.content = content;
        this.type = type;
    }
}
