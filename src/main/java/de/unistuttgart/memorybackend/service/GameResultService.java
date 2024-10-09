package de.unistuttgart.memorybackend.service;

import de.unistuttgart.memorybackend.clients.ResultClient;
import de.unistuttgart.memorybackend.data.*;
import de.unistuttgart.memorybackend.repositories.GameResultRepository;
import feign.FeignException;
import javax.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * This service handles the logic for the GameResultController.class
 */
@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
@Transactional
public class GameResultService {

    @Autowired
    ResultClient resultClient;

    @Autowired
    GameResultRepository gameResultRepository;

    int flagFirstTimeFinished = 0;

    /**
     * Casts a GameResultDTO to GameResult and saves it in the Database
     *
     * @param gameResultDTO extern gameResultDTO
     * @param userId        id of the user
     * @param accessToken   accessToken of the user
     * @throws IllegalArgumentException if at least one of the arguments is null
     */
    public void saveGameResult(
        final @Valid GameResultDTO gameResultDTO,
        final String userId,
        final String accessToken
    ) {
        if (gameResultDTO == null || userId == null || accessToken == null) {
            throw new IllegalArgumentException("gameResultDTO or userId is null");
        }
        final int resultScore = calculateResultScore(gameResultDTO.getIsFinished());
        final int rewards = calculateRewards(resultScore);

        gameResultDTO.setRewards(rewards);

        final OverworldResultDTO resultDTO = new OverworldResultDTO(
            gameResultDTO.getConfigurationAsUUID(),
            resultScore,
            userId,
            rewards
        );
        try {
            resultClient.submit(accessToken, resultDTO);
            final GameResult result = new @Valid GameResult(
                gameResultDTO.getIsFinished(),
                gameResultDTO.getConfigurationAsUUID(),
                userId,
                    rewards

            );
            gameResultRepository.save(result);
        } catch (final FeignException.BadGateway badGateway) {
            final String warning =
                "The Overworld backend is currently not available. The result was NOT saved. Please try again later";
            log.error(warning + badGateway);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, warning);
        } catch (final FeignException.NotFound notFound) {
            final String warning = "The result could not be saved. Unknown User";
            log.error(warning + notFound);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, warning);
        }
    }

    private int calculateResultScore(final Boolean isCompleted) {
        return (Boolean.TRUE.equals(isCompleted) ? 100 : 0);
    }

    /**
     * This method calculates the rewards for a memory round based on the score achieved in the
     * current game.
     *
     * - First round with a score of 100: 10 rewards
     * - Second round with a score of 100: 5 rewards
     * - For the third and all subsequent rounds with a score of 100: 1 reward per round
     *
     * @param resultScore score achieved in this round (should be 100 for rewards)
     * @return gained rewards based on the number of times a score of 100 has been achieved
     */

    private int calculateRewards(final int resultScore) {
        if (resultScore == 100) {
            if (flagFirstTimeFinished == 0) {
                flagFirstTimeFinished++;
                return 10;
            } else if (flagFirstTimeFinished == 1) {
                flagFirstTimeFinished++;
                return 5;
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

}
