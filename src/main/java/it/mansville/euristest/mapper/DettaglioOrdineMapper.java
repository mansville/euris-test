package it.mansville.euristest.mapper;

import it.mansville.euristest.dto.DettaglioOrdineResponseDTO;
import it.mansville.euristest.model.DettaglioOrdine;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DettaglioOrdineMapper {
    DettaglioOrdineResponseDTO toDto(DettaglioOrdine dettaglioOrdine);
}