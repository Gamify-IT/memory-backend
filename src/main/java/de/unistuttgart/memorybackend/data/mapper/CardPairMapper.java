package de.unistuttgart.memorybackend.data.mapper;

import de.unistuttgart.memorybackend.data.CardPair;
import de.unistuttgart.memorybackend.data.CardPairDTO;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * This mapper maps the ConfigurationDTO objects (used from external clients) and Configuration objects (used from internal code)
 */
@Mapper(componentModel = "spring")
public interface CardPairMapper {
    CardPairDTO cardPairToCardPairDTO(final CardPair cardPair);

    CardPair cardPairDTOToCardPair(final CardPairDTO cardPairDTO);

    List<CardPairDTO> cardPairsToCardPairDTOs(final List<CardPair> cardPairs);

    List<CardPair> cardPairDTOsToCardPairs(final List<CardPairDTO> cardPairDTOs);
}
