package it.mansville.euristest.mapper;

import it.mansville.euristest.command.CreaOrdineCommand;
import it.mansville.euristest.dto.DettaglioOrdineRequestDTO;
import it.mansville.euristest.dto.OrdineRequestDTO;
import it.mansville.euristest.dto.OrdineResponseDTO;
import it.mansville.euristest.model.Ordine;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {DettaglioOrdineMapper.class})
public interface OrdineMapper {
    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "nomeCliente", source = "cliente.nome")
    @Mapping(target = "cognomeCliente", source = "cliente.cognome")
    @Mapping(target = "emailCliente", source = "cliente.email")
    OrdineResponseDTO toDTO(Ordine ordine);

    CreaOrdineCommand toCommand(OrdineRequestDTO request);

    @SuppressWarnings("unused")
    default Map<String, Integer> listToMap(List<DettaglioOrdineRequestDTO> list) {
        return list.stream()
                .collect(Collectors.toMap(
                        DettaglioOrdineRequestDTO::codice,
                        DettaglioOrdineRequestDTO::quantita,
                        Integer::sum,
                        TreeMap::new
                ));
    }
}