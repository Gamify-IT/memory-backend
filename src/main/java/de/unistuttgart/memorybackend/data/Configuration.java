package de.unistuttgart.memorybackend.data;

import jakarta.persistence.*;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class Configuration {

    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;

    @OneToMany(cascade = CascadeType.ALL)
    List<CardPair> pairs;

    /**
     * The volume level that is setted by the player.
     */
    Integer volumeLevel;

    public Configuration(final List<CardPair> pairs) {
        this.pairs = pairs;
    }

    public void addCardPair(final CardPair cardPair) {
        this.pairs.add(cardPair);
    }

    public void removeCardPair(final CardPair cardPair) {
        this.pairs.remove(cardPair);
    }

    @Override
    public Configuration clone() { //NOSONAR
        return new Configuration(this.pairs.stream().map(CardPair::clone).toList());
    }
}
