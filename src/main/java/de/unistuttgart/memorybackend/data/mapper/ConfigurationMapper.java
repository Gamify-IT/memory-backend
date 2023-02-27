package de.unistuttgart.memorybackend.data.mapper;

import de.unistuttgart.memorybackend.data.Configuration;
import de.unistuttgart.memorybackend.data.ConfigurationDTO;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * This mapper maps the ConfigurationDTO objects (used from external clients) and Configuration objects (used from internal code)
 */
@Mapper(componentModel = "spring")
public interface ConfigurationMapper {
    ConfigurationDTO configurationToConfigurationDTO(final Configuration configuration);

    Configuration configurationDTOToConfiguration(final ConfigurationDTO configurationDTO);

    Configuration configurationDTOsToConfiguration(final ConfigurationDTO configurationDTOs);

    List<ConfigurationDTO> configurationsToConfigurationDTOs(final List<Configuration> configurations);
}
