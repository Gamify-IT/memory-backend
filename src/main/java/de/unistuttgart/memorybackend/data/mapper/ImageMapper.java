package de.unistuttgart.memorybackend.data.mapper;

import de.unistuttgart.memorybackend.data.Image;
import de.unistuttgart.memorybackend.data.ImageDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageDTO imageToImageDTO(final Image image);

    Image imageDTOToImage(final ImageDTO imageDTO);
}
