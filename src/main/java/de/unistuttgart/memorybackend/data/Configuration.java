package de.unistuttgart.memorybackend.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Configuration {

    @Id
    UUID id;

    @OneToMany
    List<CardPair> pairs;

    public Configuration(final List<CardPair> pairs) {
        this.pairs = pairs;
    }

    public void addCardPair(final CardPair cardPair){
        this.pairs.add(cardPair);
    }

    public void removeCardPair(final CardPair cardPair){
        this.pairs.remove(cardPair);
    }
}
