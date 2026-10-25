package it.mansville.euristest.mapper;

import it.mansville.euristest.dto.ProdottoRequestDTO;
import it.mansville.euristest.dto.ProdottoResponseDTO;
import it.mansville.euristest.model.Prodotto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProdottoMapper {
    Prodotto toEntity(ProdottoRequestDTO prodottoDTO);

    ProdottoResponseDTO toDTO(Prodotto prodotto);
}