package de.unistuttgart.memorybackend.data;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ConfigurationDTO {

    /**
     * A unique identifier for the game result.
     */
    @Nullable
    private UUID id;

    List<CardPairDTO> pairs;

    /**
     * The volume level that is setted by the player.
     */
    Integer volumeLevel;

    public ConfigurationDTO(final List<CardPairDTO> pairs) {
        this.pairs = pairs;
    }

    public boolean equalsContent(final ConfigurationDTO other) {
        if (this == other) return true;
        if (other == null) return false;
        return Objects.equals(pairs, other.pairs);
    }
}
