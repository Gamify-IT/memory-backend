package de.unistuttgart.memorybackend.data;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum CardType {
    IMAGE,
    TEXT,
    MARKDOWN,
}
