package de.unistuttgart.memorybackend.controller;

import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.memorybackend.data.GameResultDTO;
import de.unistuttgart.memorybackend.service.GameResultService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Import({ JWTValidatorService.class })
@RestController
@RequestMapping("/results")
@Slf4j
@Validated
public class GameResultController {

    @Autowired
    GameResultService gameResultService;

    @Autowired
    private JWTValidatorService jwtValidatorService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public GameResultDTO saveGameResult(
        @CookieValue("access_token") final String accessToken,
        @Valid @RequestBody final GameResultDTO gameResultDTO
    ) {
        jwtValidatorService.validateTokenOrThrow(accessToken);
        final String userId = jwtValidatorService.extractUserId(accessToken);
        log.debug("save game result for userId {}: {}", userId, gameResultDTO);
        gameResultService.saveGameResult(gameResultDTO, userId, accessToken);
        return gameResultDTO;
    }
}
