package it.mansville.euristest.mapper;

import it.mansville.euristest.dto.ClienteRequestDTO;
import it.mansville.euristest.dto.ClienteResponseDTO;
import it.mansville.euristest.model.Cliente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ClienteMapper {
    Cliente toEntity(ClienteRequestDTO clienteDTO);

    ClienteResponseDTO toDTO(Cliente cliente);
}