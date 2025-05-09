package de.unistuttgart.memorybackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.memorybackend.data.*;
import de.unistuttgart.memorybackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.memorybackend.repositories.ConfigurationRepository;
import de.unistuttgart.memorybackend.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Import({ JWTValidatorService.class })
@RestController
@RequestMapping("/configurations")
@Slf4j
@Validated
public class ConfigController {

    public static final List<String> LECTURER = List.of("lecturer");
    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private JWTValidatorService jwtValidatorService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @PostConstruct
    public void createDummyData() {
        List<CardPair> pairs = Arrays.asList(
            new CardPair(new Card("text", CardType.TEXT), new Card("text", CardType.TEXT)),
            new CardPair(new Card("text", CardType.TEXT), new Card("text", CardType.TEXT)),
            new CardPair(new Card("text", CardType.TEXT), new Card("text", CardType.TEXT)),
            new CardPair(new Card("text", CardType.TEXT), new Card("text", CardType.TEXT)),
            new CardPair(new Card("text", CardType.TEXT), new Card("text", CardType.TEXT)),
            new CardPair(new Card("text", CardType.TEXT), new Card("text", CardType.TEXT))
        );
        final Configuration configuration = new Configuration(pairs);
        final Configuration savedConfig = configurationRepository.save(configuration);
        log.debug("Create dummy configuration with id" + savedConfig.getId());
    }

    @Operation(summary = "Add an image to be used in a memory configuration")
    @PostMapping("/images")
    @ResponseStatus(HttpStatus.CREATED)
    public ImageDTO addImage(@CookieValue("access_token") final String accessToken, @RequestParam("uuid") UUID uuid,
                             @RequestParam("image") MultipartFile file) throws IOException {
        jwtValidatorService.validateTokenOrThrow(accessToken);

        byte[] imageBytes = file.getBytes();
        if (imageBytes.length == 0) {
            throw new IllegalArgumentException("The uploaded file is empty.");
        }

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setImageUUID(uuid);
        imageDTO.setImage(imageBytes);

        return configService.addImage(imageDTO);
    }

    @Operation(summary = "Retrieve an image")
    @GetMapping("/images/{uuid}")
    @Transactional
    public ImageDTO getImage(@CookieValue("access_token") final String accessToken, @PathVariable final String uuid) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        return configService.getImage(UUID.fromString(uuid));
    }


    @Operation(summary = "Get all configurations")
    @GetMapping("")
    public List<ConfigurationDTO> getConfigurations(@CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get all configurations");
        return configurationMapper.configurationsToConfigurationDTOs(configurationRepository.findAll());
    }

    @GetMapping("/{id}")
    public ConfigurationDTO getConfiguration(
            @CookieValue("access_token") final String accessToken,
            @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get configuration {}", id);
        return configurationMapper.configurationToConfigurationDTO(configService.getConfiguration(id));
    }

    @GetMapping("/{id}/volume")
    public ConfigurationDTO getAllConfiguration(
            @CookieValue("access_token") final String accessToken,
            @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get configuration {}", id);
        return configurationMapper.configurationToConfigurationDTO(
                configService.getAllConfigurations(id, accessToken)
        );
    }

    @Operation(summary = "Create a new configuration")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigurationDTO createConfiguration(
        @CookieValue("access_token") final String accessToken,
        @Valid @RequestBody final ConfigurationDTO configurationDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("create configuration {}", configurationDTO);
        return configService.saveConfiguration(configurationDTO);
    }

    @Operation(summary = "Update a configuration")
    @PutMapping("/{id}")
    public ConfigurationDTO updateConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @Valid @RequestBody final ConfigurationDTO configurationDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("update configuration {} with {}", id, configurationDTO);
        return configService.updateConfiguration(id, configurationDTO);
    }

    @Operation(summary = "Delete a configuration")
    @DeleteMapping("/{id}")
    public ConfigurationDTO deleteConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("delete configuration {}", id);
        return configService.deleteConfiguration(id);
    }

    @Operation(summary = "Add multiple card pairs to a configuration")
    @PostMapping("/{id}/cardPair")
    @ResponseStatus(HttpStatus.CREATED)
    public CardPairDTO addCardPairDTOToConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @Valid @RequestBody final CardPairDTO cardPairDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("add card pair {} to configuration {}", cardPairDTO, id);
        return configService.addCardPairToConfiguration(id, cardPairDTO);
    }

    @Operation(summary = "Delete a card pair from a configuration")
    @DeleteMapping("/{id}/cardPair/{cardPairId}")
    public CardPairDTO removeCardPairDTOFromConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @PathVariable final UUID cardPairId
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("remove card pairs {} from configuration {}", cardPairId, id);
        return configService.removeCardPairFromConfiguration(id, cardPairId);
    }

    @Operation(summary = "Update a card pair in a configuration")
    @PutMapping("/{id}/cardPair/{cardPairId}")
    public CardPairDTO updateCardPairFromConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @PathVariable final UUID cardPairId,
        @Valid @RequestBody final CardPairDTO cardPairDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        log.debug("update card pair {} with {} for configuration {}", cardPairId, cardPairDTO, id);
        return configService.updateCardPairFromConfiguration(id, cardPairId, cardPairDTO);
    }

    @PostMapping("/{id}/clone")
    @ResponseStatus(HttpStatus.CREATED)
    public UUID cloneConfiguration(@CookieValue("access_token") final String accessToken, @PathVariable final UUID id) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, LECTURER);
        return configService.cloneConfiguration(id);
    }
}
