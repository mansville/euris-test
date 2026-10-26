package it.mansville.euristest.mapper;

import it.mansville.euristest.dto.ProdottoRequestDTO;
import it.mansville.euristest.dto.ProdottoResponseDTO;
import it.mansville.euristest.model.Prodotto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProdottoMapper {
    Prodotto toEntity(ProdottoRequestDTO prodottoDTO);

    ProdottoResponseDTO toDTO(Prodotto prodotto);
}