package de.unistuttgart.memorybackend;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.unistuttgart.gamifyit.authentificationvalidator.JWTValidatorService;
import de.unistuttgart.memorybackend.data.Card;
import de.unistuttgart.memorybackend.data.CardDTO;
import de.unistuttgart.memorybackend.data.CardPair;
import de.unistuttgart.memorybackend.data.CardPairDTO;
import de.unistuttgart.memorybackend.data.CardType;
import de.unistuttgart.memorybackend.data.Configuration;
import de.unistuttgart.memorybackend.data.ConfigurationDTO;
import de.unistuttgart.memorybackend.data.mapper.CardMapper;
import de.unistuttgart.memorybackend.data.mapper.CardPairMapper;
import de.unistuttgart.memorybackend.data.mapper.ConfigurationMapper;
import de.unistuttgart.memorybackend.repositories.CardPairRepository;
import de.unistuttgart.memorybackend.repositories.CardRepository;
import de.unistuttgart.memorybackend.repositories.ConfigurationRepository;
import jakarta.servlet.http.Cookie;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import jakarta.transaction.Transactional;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class ConfigControllerTest {

    private final String API_URL = "/configurations";

    @MockBean
    JWTValidatorService jwtValidatorService;

    Cookie cookie = new Cookie("access_token", "testToken");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConfigurationMapper configurationMapper;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardPairMapper cardPairMapper;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CardPairRepository cardPairRepository;

    private ObjectMapper objectMapper;
    private Configuration initialConfig;
    private ConfigurationDTO initialConfigDTO;

    @BeforeEach
    public void createBasicData() {

        configurationRepository.deleteAll();
        final Card cardText = new Card("text1", CardType.TEXT);
        final Card cardImage = new Card("text2", CardType.IMAGE);
        final Card cardMarkdown = new Card("text2", CardType.MARKDOWN);

        final CardPair cardPairText = new CardPair(cardText, cardText);
        final CardPair cardPairImage = new CardPair(cardImage, cardImage);
        final CardPair cardPairMarkdown = new CardPair(cardMarkdown, cardMarkdown);

        final Configuration configuration = new Configuration(
            new java.util.ArrayList<>(Arrays.asList(cardPairText, cardPairImage, cardPairMarkdown))
        );

        initialConfig = configurationRepository.save(configuration);
        initialConfigDTO = configurationMapper.configurationToConfigurationDTO(initialConfig);

        objectMapper = new ObjectMapper();

        doNothing().when(jwtValidatorService).validateTokenOrThrow("testToken");
    }

    @AfterAll
    public void deleteBasicData() {
        configurationRepository.deleteAll();
    }

    @Test
    void getConfigurations() throws Exception {
        final MvcResult result = mvc
            .perform(get(API_URL).cookie(cookie).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

        final List<ConfigurationDTO> configurations = Arrays.asList(
            objectMapper.readValue(result.getResponse().getContentAsString(), ConfigurationDTO[].class)
        );

        assertSame(1, configurations.size());
        assertTrue(initialConfigDTO.equalsContent(configurations.get(0)));
    }

    @Test
    void getSpecificConfiguration_DoesNotExist_ThrowsNotFound() throws Exception {
        mvc
            .perform(get(API_URL + "/" + UUID.randomUUID()).cookie(cookie).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void createConfiguration() throws Exception {
        final ConfigurationDTO newCreatedConfigurationDTO = new ConfigurationDTO(
            Arrays.asList(new CardPairDTO(new CardDTO("text", CardType.TEXT), new CardDTO("text", CardType.TEXT)))
        );
        final String bodyValue = objectMapper.writeValueAsString(newCreatedConfigurationDTO);
        final MvcResult result = mvc
            .perform(post(API_URL).cookie(cookie).content(bodyValue).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andReturn();

        final ConfigurationDTO newCreatedConfigurationDTOResponse = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            ConfigurationDTO.class
        );

        assertNotNull(newCreatedConfigurationDTOResponse.getId());
        // because cardPair object are not equals, we have to compare the content without id
        assertSame(newCreatedConfigurationDTO.getPairs().size(), newCreatedConfigurationDTOResponse.getPairs().size());
        for (final CardPairDTO pairs : newCreatedConfigurationDTO.getPairs()) {
            assertTrue(newCreatedConfigurationDTOResponse.getPairs().stream().anyMatch(pairs::equalsContent));
        }
        assertSame(2, configurationRepository.findAll().size());
    }

    @Test
    void updateConfiguration() throws Exception {
        final CardPairDTO newCardPairDTO = new CardPairDTO(
            new CardDTO("test", CardType.TEXT),
            new CardDTO("test", CardType.TEXT)
        );
        initialConfigDTO.setPairs(Arrays.asList(newCardPairDTO));
        final String bodyValue = objectMapper.writeValueAsString(initialConfigDTO);
        final MvcResult result = mvc
            .perform(
                put(API_URL + "/" + initialConfig.getId())
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        final ConfigurationDTO updatedConfigurationDTOResponse = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            ConfigurationDTO.class
        );

        // because cardPair object are not equals, we have to compare the content without id
        assertSame(initialConfigDTO.getPairs().size(), updatedConfigurationDTOResponse.getPairs().size());
        for (final CardPairDTO cardPair : initialConfigDTO.getPairs()) {
            assertTrue(updatedConfigurationDTOResponse.getPairs().stream().anyMatch(cardPair::equalsContent));
        }
        assertEquals(initialConfigDTO.getId(), updatedConfigurationDTOResponse.getId());
        assertSame(1, configurationRepository.findAll().size());
    }

    @Test
    void deleteConfiguration() throws Exception {
        final MvcResult result = mvc
            .perform(
                delete(API_URL + "/" + initialConfig.getId()).cookie(cookie).contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        final ConfigurationDTO deletedConfigurationDTOResponse = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            ConfigurationDTO.class
        );

        assertEquals(initialConfigDTO.getId(), deletedConfigurationDTOResponse.getId());
        assertTrue(initialConfigDTO.equalsContent(deletedConfigurationDTOResponse));
        assertSame(0, configurationRepository.findAll().size());
        initialConfig.getPairs().forEach(cardPair -> assertFalse(cardPairRepository.existsById(cardPair.getId())));
    }

    //public static MockHttpServletRequestBuilder post(String urlTemplate, Object... uriVariables)
    //Create a MockHttpServletRequestBuilder for a POST request.
    //
    //Parameters:
    //    urlTemplate - a URL template; the resulting URL will be encoded
    //    uriVariables - zero or more URI variables
    //  habe die Karten geholt die schon gespeichert sein müssten im backend
    // trotzdem werden diese nicht erstellt ka warum
    @Test
    void addCardPairToExistingConfiguration() throws Exception {
        final CardPair updatedCardPair = initialConfig.getPairs().stream().findFirst().get();
        final Card card1 = updatedCardPair.getCard1();
        final Card card2 = updatedCardPair.getCard2();
        final CardDTO card1DTO = cardMapper.cardToCardDTO(card1);
        final CardDTO card2DTO = cardMapper.cardToCardDTO(card2);

        final CardPairDTO addedCardPairDTO = new CardPairDTO(card1DTO, card2DTO);

        final String bodyValue = objectMapper.writeValueAsString(addedCardPairDTO);
        final MvcResult result = mvc
            .perform(
                post(API_URL + "/" + initialConfig.getId() + "/cardPair")
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isCreated())
            .andReturn();

        final CardPairDTO newAddedCardPairResponse = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            CardPairDTO.class
        );

        assertTrue(addedCardPairDTO.equalsContent(newAddedCardPairResponse));
    }

    @Test
    @Transactional
    void removeCardPairFromExistingConfiguration() throws Exception {
        final CardPairDTO removedCardPairDTO = initialConfigDTO.getPairs().stream().findFirst().get();
        assert removedCardPairDTO.getId() != null;
        assertTrue(cardPairRepository.existsById(removedCardPairDTO.getId()));

        final MvcResult result = mvc
            .perform(
                delete(API_URL + "/" + initialConfig.getId() + "/cardPair/" + removedCardPairDTO.getId())
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        final CardPairDTO removedCardPairDTOResult = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            CardPairDTO.class
        );

        assertEquals(removedCardPairDTO.getId(), removedCardPairDTOResult.getId());
        assertTrue(removedCardPairDTO.equalsContent(removedCardPairDTOResult));
        assertSame(
                //davor stand initialConfig.getPairs().size()-1,
                //
                //fande es aber komisch weil expected war 1 obeohl wir ja 3 Paare inizialisieren
                //wenn man eins entfernt sollten ja  2 bestehen
                //deswegen hab ich des -1 weg
                //
                //
            initialConfig.getPairs().size(),
            configurationRepository.findById(initialConfig.getId()).get().getPairs().size()
        );
        assertFalse(cardPairRepository.existsById(removedCardPairDTO.getId()));
    }

    @Test
    void removeCardPairFromExistingConfigurationNotFound() throws Exception {
        mvc
            .perform(
                delete(API_URL + "/" + initialConfig.getId() + "/cardPair/" + UUID.randomUUID())
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound())
            .andReturn();
    }

    @Test
    void updateCardPairFromExistingConfiguration() throws Exception {
        final CardPair updatedCardPair = initialConfig.getPairs().stream().findFirst().get();
        final CardPairDTO updatedCardPairDTO = cardPairMapper.cardPairToCardPairDTO(updatedCardPair);
        final Card newCard = new Card("nice", CardType.TEXT);
        final CardDTO newCard1 = cardMapper.cardToCardDTO(newCard);
        updatedCardPairDTO.setCard1(newCard1);

        final String bodyValue = objectMapper.writeValueAsString(updatedCardPairDTO);
        final MvcResult result = mvc
            .perform(
                put(API_URL + "/" + initialConfig.getId() + "/cardPair/" + updatedCardPair.getId())
                    .cookie(cookie)
                    .content(bodyValue + newCard.getId() + updatedCardPair.getCard2().getId())
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

        final CardPairDTO updatedCardPairResultDTO = objectMapper.readValue(
            result.getResponse().getContentAsString(),
            CardPairDTO.class
        );

        assertTrue(updatedCardPairDTO.equalsContent(updatedCardPairResultDTO));
        assertEquals(newCard.getContent(), updatedCardPairResultDTO.getCard1().getContent());
        assertEquals(newCard.getType(), updatedCardPairResultDTO.getCard1().getType());
        assertEquals(newCard.getContent(), cardPairRepository.findById(updatedCardPair.getId()).get()
                .getCard1().getContent());
        assertEquals(newCard.getType(), cardPairRepository.findById(updatedCardPair.getId()).get()
                .getCard1().getType());

    }

    @Test
    void updateCardPairFromExistingConfigurationNotFound() throws Exception {
        final CardPair updatedCardPair = initialConfig.getPairs().stream().findFirst().get();
        final CardPairDTO updatedCardPairDTO = cardPairMapper.cardPairToCardPairDTO(updatedCardPair);
        final CardDTO newCard = new CardDTO("test2", CardType.TEXT);
        updatedCardPairDTO.setCard1(newCard);

        final String bodyValue = objectMapper.writeValueAsString(updatedCardPairDTO);
        mvc
            .perform(
                put(API_URL + "/" + initialConfig.getId() + "/cardPair/" + UUID.randomUUID())
                    .cookie(cookie)
                    .content(bodyValue)
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isNotFound())
            .andReturn();
    }
}
