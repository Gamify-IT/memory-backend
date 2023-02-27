package de.unistuttgart.memorybackend.data;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class Configuration {

    UUID id;

    CardPair[] pairs;

    public Configuration(final CardPair[] pairs){
    this.pairs = pairs;
    }
}
