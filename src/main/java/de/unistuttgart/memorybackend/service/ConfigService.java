package de.unistuttgart.memorybackend.service;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.memorybackend.clients.OverworldClient;
import de.unistuttgart.memorybackend.data.*;
import de.unistuttgart.memorybackend.data.mapper.CardPairMapper;
import de.unistuttgart.memorybackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.memorybackend.data.mapper.ImageMapper;
import de.unistuttgart.memorybackend.repositories.CardPairRepository;
import de.unistuttgart.memorybackend.repositories.ConfigurationRepository;

import java.util.*;
import java.util.stream.Collectors;

import de.unistuttgart.memorybackend.repositories.ImageRepository;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@ComponentScan("de.unistuttgart.memorybackend.data.mapper")
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class ConfigService {

    @Autowired
    CardPairMapper cardPairMapper;

    @Autowired
    ConfigurationMapper configurationMapper;

    @Autowired
    ConfigurationRepository configurationRepository;

    @Autowired
    CardPairRepository cardPairRepository;

    @Autowired
    private OverworldClient overworldClient;

    @Autowired
    private JWTValidatorService jwtValidatorService;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ImageMapper imageMapper;

    /**
     * Search a configuration by given id
     *
     * @param id the id of the configuration searching for
     * @return the found configuration
     * @throws ResponseStatusException (404) when configuration by configuration id could not be found
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public Configuration getConfiguration(final UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        return configurationRepository
            .findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("There is no configuration with id %s.", id)
                )
            );
    }

    /**
     * Search a configuration by given id and get volume level from overworld-backend
     *
     * @param id the id of the configuration searching for
     * @param accessToken the users access token
     * @return the found configuration
     * @throws ResponseStatusException  when configuration by configurationName could not be found
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public Configuration getAllConfigurations(final UUID id, final String accessToken) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        final String userId = jwtValidatorService.extractUserId(accessToken);

        KeybindingDTO keyBindingVolumeLevel = overworldClient.getKeybindingStatistic(userId, "VOLUME_LEVEL", accessToken);
        Integer volumeLevel;
        if (keyBindingVolumeLevel.getKey() == null || keyBindingVolumeLevel.getKey().isEmpty()) {
            volumeLevel = 0;
        }
        else {
            try {
                volumeLevel = Integer.parseInt(keyBindingVolumeLevel.getKey());
            } catch (NumberFormatException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid volume level format.");
            }
        }

        Configuration config = configurationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("There is no configuration with id %s.", id)
                        )
                );
        config.setVolumeLevel(volumeLevel);
        return configurationRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                String.format("There is no configuration with id %s.", id)
                        )
                );
    }

    /**
     * Save a configuration
     *
     * @param configurationDTO configuration that should be saved
     * @return the saved configuration as DTO
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public ConfigurationDTO saveConfiguration(final ConfigurationDTO configurationDTO) {
        if (configurationDTO == null) {
            throw new IllegalArgumentException("configurationDTO is null");
        }
        final Configuration savedConfiguration = configurationRepository.save(
            configurationMapper.configurationDTOToConfiguration(configurationDTO)
        );
        return configurationMapper.configurationToConfigurationDTO(savedConfiguration);
    }

    /**
     * Update a configuration
     *
     * @param id               the id of the configuration that should be updated
     * @param configurationDTO configuration that should be updated
     * @return the updated configuration as DTO
     * @throws ResponseStatusException (404) when configuration with the id does not exist
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public ConfigurationDTO updateConfiguration(final UUID id, final ConfigurationDTO configurationDTO) {
        if (id == null || configurationDTO == null) {
            throw new IllegalArgumentException("id or configurationDTO is null");
        }
        final Configuration configuration = getConfiguration(id);
        configuration.setPairs(cardPairMapper.cardPairDTOsToCardPairs(configurationDTO.getPairs()));
        final Configuration updatedConfiguration = configurationRepository.save(configuration);
        return configurationMapper.configurationToConfigurationDTO(updatedConfiguration);
    }

    /**
     * Delete a configuration
     *
     * @param id the id of the configuration that should be updated
     * @return the deleted configuration as DTO
     * @throws ResponseStatusException (404) when configuration with the id does not exist
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public ConfigurationDTO deleteConfiguration(final UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("id is null");
        }
        final Configuration configuration = getConfiguration(id);
        configurationRepository.delete(configuration);
        return configurationMapper.configurationToConfigurationDTO(configuration);
    }

    /**
     * Add a card pair to specific configuration
     *
     * @param id          the id of the configuration where a card pair should be added
     * @param cardPairDTO the card pair that should be added
     * @return the added card pair as DTO
     * @throws ResponseStatusException (404) when configuration with the id does not exist
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public CardPairDTO addCardPairToConfiguration(final UUID id, final CardPairDTO cardPairDTO) {
        if (id == null || cardPairDTO == null) {
            throw new IllegalArgumentException("id or cardPairDTO is null");
        }
        final Configuration configuration = getConfiguration(id);
        final CardPair cardPair = cardPairRepository.save(cardPairMapper.cardPairDTOToCardPair(cardPairDTO));
        configuration.addCardPair(cardPair);
        configurationRepository.save(configuration);
        return cardPairMapper.cardPairToCardPairDTO(cardPair);
    }

    /**
     * Delete a card pair from a specific configuration
     *
     * @param id         the id of the configuration where a card pair should be removed
     * @param cardPairId the id of the card pair that should be deleted
     * @return the deleted card pair as DTO
     * @throws ResponseStatusException (404) when configuration with the id or card pair with id does not exist
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public CardPairDTO removeCardPairFromConfiguration(final UUID id, final UUID cardPairId) {
        if (id == null || cardPairId == null) {
            throw new IllegalArgumentException("id or cardPairId is null");
        }
        final Configuration configuration = getConfiguration(id);
        final CardPair cardPair = getCardPairInConfiguration(cardPairId, configuration)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Card pair with ID %s does not exist in configuration %s.", cardPairId, configuration)
                )
            );
        configuration.removeCardPair(cardPair);
        configurationRepository.save(configuration);
        cardPairRepository.delete(cardPair);
        return cardPairMapper.cardPairToCardPairDTO(cardPair);
    }

    /**
     * Update a card pair from a specific configuration
     *
     * @param id          the id of the configuration where a card pair should be updated
     * @param cardPairId  the id of the card pair that should be updated
     * @param cardPairDTO the content of the card pair that should be updated
     * @return the updated card pair as DTO
     * @throws ResponseStatusException (404) when configuration with the id or card pair with id does not exist
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public CardPairDTO updateCardPairFromConfiguration(
        final UUID id,
        final UUID cardPairId,
        final CardPairDTO cardPairDTO
    ) {
        if (id == null || cardPairId == null || cardPairDTO == null) {
            throw new IllegalArgumentException("id or cardPairId or cardPairDTO is null");
        }
        final Configuration configuration = getConfiguration(id);
        if (getCardPairInConfiguration(cardPairId, configuration).isEmpty()) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND,
                String.format("CardPair with ID %s does not exist in configuration %s.", cardPairId, configuration)
            );
        }
        final CardPair cardPair = cardPairMapper.cardPairDTOToCardPair(cardPairDTO);
        cardPair.setId(cardPairId);
        final CardPair savedCardPair = cardPairRepository.save(cardPair);
        return cardPairMapper.cardPairToCardPairDTO(savedCardPair);
    }

    /**
     * Returns the cardPair if the configuration contains the cardPairId
     *
     * @param cardPairId    id of searched card pair
     * @param configuration configuration in which the card pair is part of
     * @return an optional of the card pair
     * @throws ResponseStatusException (404) when card pair with the id in the given configuration does not exist
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    private Optional<CardPair> getCardPairInConfiguration(final UUID cardPairId, final Configuration configuration) {
        if (cardPairId == null || configuration == null) {
            throw new IllegalArgumentException("cardPairId or configuration is null");
        }
        return configuration
            .getPairs()
            .parallelStream()
            .filter(filteredCardPair -> filteredCardPair.getId().equals(cardPairId))
            .findAny();
    }

    public UUID cloneConfiguration(final UUID id) {
        Configuration config = configurationRepository
            .findById(id)
            .orElseThrow(() ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    String.format("Configuration with id %s not found", id)
                )
            );
        Configuration cloneConfig = config.clone();
        cloneConfig = configurationRepository.save(cloneConfig);
        return cloneConfig.getId();
    }

    public ImageDTO addImage(final ImageDTO imageDTO) {
        imageRepository.save(imageMapper.imageDTOToImage(imageDTO));
        return imageDTO;
    }

    public ImageDTO getImage(UUID uuid) {
        Image image = imageRepository.findByImageUUID(uuid);
        return imageMapper.imageToImageDTO(image);
    }


}