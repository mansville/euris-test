package it.mansville.euristest.mapper;

import it.mansville.euristest.dto.DettaglioOrdineResponseDTO;
import it.mansville.euristest.model.DettaglioOrdine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DettaglioOrdineMapper {
    @Mapping(target = "idProdotto", source = "prodotto.id")
    @Mapping(target = "codiceProdotto", source = "prodotto.codice")
    @Mapping(target = "nomeProdotto", source = "prodotto.nome")
    DettaglioOrdineResponseDTO toDto(DettaglioOrdine dettaglioOrdine);
}