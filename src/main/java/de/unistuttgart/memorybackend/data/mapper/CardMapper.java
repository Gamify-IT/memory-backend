package de.unistuttgart.memorybackend.data.mapper;

import de.unistuttgart.memorybackend.data.Card;
import de.unistuttgart.memorybackend.data.CardDTO;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * This mapper maps the ConfigurationDTO objects (used from external clients) and Configuration objects (used from internal code)
 */
@Mapper(componentModel = "spring")
public interface CardMapper {
    CardDTO cardToCardDTO(final Card card);

    Card cardDTOToCard(final CardDTO cardDTO);

    List<CardDTO> cardsToCardDTOs(final List<Card> cards);

    List<Card> cardDTOsToCards(final List<CardDTO> cardDTOs);
}
