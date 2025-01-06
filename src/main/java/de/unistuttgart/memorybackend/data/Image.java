package de.unistuttgart.memorybackend.data;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "image_table")
@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long imageID;

    private UUID uuid;

    @Column(name = "picByte", columnDefinition="bytea")
    private byte[] picByte;
}