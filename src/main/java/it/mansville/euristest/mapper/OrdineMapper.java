package it.mansville.euristest.mapper;

import it.mansville.euristest.dto.OrdineResponseDTO;
import it.mansville.euristest.model.Ordine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DettaglioOrdineMapper.class})
public interface OrdineMapper {
    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "cognomeCliente", source = "cliente.cognome")
    @Mapping(target = "emailCliente", source = "cliente.email")
    OrdineResponseDTO toDTO(Ordine ordine);
}