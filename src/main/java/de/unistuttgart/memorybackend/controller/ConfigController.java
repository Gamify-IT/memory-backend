package de.unistuttgart.memorybackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.memorybackend.data.CardPairDTO;
import de.unistuttgart.memorybackend.data.ConfigurationDTO;
import de.unistuttgart.memorybackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.memorybackend.repositories.ConfigurationRepository;
import de.unistuttgart.memorybackend.service.ConfigService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Import({ JWTValidatorService.class })
@RestController
@RequestMapping("/configurations")
@Slf4j
@Validated
public class ConfigController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private JWTValidatorService jwtValidatorService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @Operation(summary = "Get all configurations")
    @GetMapping("")
    public List<ConfigurationDTO> getConfigurations(@CookieValue("access_token") final String accessToken) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get all configurations");
        return configurationMapper.configurationsToConfigurationDTOs(configurationRepository.findAll());
    }

    @Operation(summary = "Get a specific configuration by its id")
    @GetMapping("/{id}")
    public ConfigurationDTO getConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        log.debug("get configuration {}", id);
        return configurationMapper.configurationToConfigurationDTO(configService.getConfiguration(id));
    }

    @Operation(summary = "Create a new configuration")
    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigurationDTO createConfiguration(
        @CookieValue("access_token") final String accessToken,
        @Valid @RequestBody final ConfigurationDTO configurationDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
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
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
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
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("delete configuration {}", id);
        return configService.deleteConfiguration(id);
    }

    @Operation(summary = "Add multiple card pairs to a configuration")
    @PostMapping("/{id}/cardPairs")
    @ResponseStatus(HttpStatus.CREATED)
    public CardPairDTO addCardPairDTOToConfiguration(
        @CookieValue("access_token") final String accessToken,
        @PathVariable final UUID id,
        @Valid @RequestBody final CardPairDTO cardPairDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
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
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
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
        jwtValidatorService.hasRolesOrThrow(accessToken, List.of("lecturer"));
        log.debug("update card pair {} with {} for configuration {}", cardPairId, cardPairDTO, id);
        return configService.updateCardPairFromConfiguration(id, cardPairId, cardPairDTO);
    }
}
