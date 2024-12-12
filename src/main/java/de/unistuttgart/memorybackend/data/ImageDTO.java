package de.unistuttgart.memorybackend.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class ImageDTO {
    @NotNull(message = "imageUUID cannot be null")
    private UUID configurationAsUUID;

    private File image;
}
