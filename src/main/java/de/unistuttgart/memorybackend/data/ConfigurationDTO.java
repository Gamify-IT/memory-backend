package de.unistuttgart.memorybackend.data;

import java.util.List;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfigurationDTO {

    /**
     * A unique identifier for the game result.
     */
    @Nullable
    private UUID id;

    List<CardPairDTO> pairs;

        public ConfigurationDTO(final List<CardPairDTO> pairs){
        this.pairs = pairs;
        }
}
