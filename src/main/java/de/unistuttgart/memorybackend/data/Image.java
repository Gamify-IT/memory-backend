package de.unistuttgart.memorybackend.data;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(generator = "uuid")
    UUID id;
    private UUID imageUUID;
    private byte[] image;
}