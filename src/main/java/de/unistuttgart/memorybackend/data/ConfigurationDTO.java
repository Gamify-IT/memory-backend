package de.unistuttgart.memorybackend.data;

import java.util.UUID;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationDTO {

    /**
     * A unique identifier for the game result.
     */
    @Nullable
    private UUID id;

    CardPairDTO[] pairs;

        public ConfigurationDTO(final CardPairDTO[] pairs){
        this.pairs = pairs;
        }
}
