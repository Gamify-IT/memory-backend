package de.unistuttgart.memorybackend.data;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "image_table")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageID;

    private UUID uuid;

    @Column(name = "picByte", length = 5000)
    private byte[] picByte;
}