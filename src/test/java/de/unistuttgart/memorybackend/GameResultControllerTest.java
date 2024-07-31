package de.unistuttgart.memorybackend;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.memorybackend.data.*;
import de.unistuttgart.memorybackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.memorybackend.repositories.ConfigurationRepository;
import de.unistuttgart.memorybackend.repositories.GameResultRepository;
import jakarta.servlet.http.Cookie;
import java.io.IOException;
import java.util.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
@EnableConfigurationProperties
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { WireMockConfig.class })
class GameResultControllerTest {

    private final String API_URL = "/results";

    @MockBean
    JWTValidatorService jwtValidatorService;

    Cookie cookie = new Cookie("access_token", "testToken");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private GameResultRepository gameResultRepository;

    @Autowired
    private WireMockServer mockResultsService;

    private ObjectMapper objectMapper;
    private Configuration initialConfig;
    private ConfigurationDTO initialConfigDTO;
    private CardPair initialCardPair1;
    private CardPair initialCardPair2;

    @BeforeEach
    public void createBasicData() throws IOException {
        ResultMocks.setupMockBooksResponse(mockResultsService);
        configurationRepository.deleteAll();
        initialCardPair1 = new CardPair(new Card("täst", CardType.TEXT), new Card("täst", CardType.TEXT));

        initialCardPair2 = new CardPair(new Card("test", CardType.TEXT), new Card("test", CardType.TEXT));

        final Configuration configuration = new Configuration();
        configuration.setPairs(Arrays.asList(initialCardPair1, initialCardPair2));

        initialConfig = configurationRepository.save(configuration);
        initialConfigDTO = configurationMapper.configurationToConfigurationDTO(initialConfig);
        initialConfig
            .getPairs()
            .stream()
            .filter(cardPair -> cardPair.getCard1().equals(initialCardPair1.getCard1()))
            .forEach(cardPair -> initialCardPair1 = cardPair);
        initialConfig
            .getPairs()
            .stream()
            .filter(cardPair -> cardPair.getCard1().equals(initialCardPair2.getCard1()))
            .forEach(cardPair -> initialCardPair2 = cardPair);

        objectMapper = new ObjectMapper();

        doNothing().when(jwtValidatorService).validateTokenOrThrow("testToken");
        when(jwtValidatorService.extractUserId("testToken")).thenReturn("testUser");
    }

    @AfterEach
    void deleteBasicData() {
        gameResultRepository.deleteAll();
        configurationRepository.deleteAll();
    }

    @Test
    void saveGameResult() throws Exception {
        final GameResultDTO gameResultDTO = new GameResultDTO(true, UUID.randomUUID(), "123", 10);

        final String bodyValue = objectMapper.writeValueAsString(gameResultDTO);
        final MvcResult result = mvc
            .perform(post(API_URL).cookie(cookie).content(bodyValue).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        final GameResultDTO createdGameResultDTO = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            GameResultDTO.class
        );

        assertEquals(gameResultDTO, createdGameResultDTO);
    }
}
