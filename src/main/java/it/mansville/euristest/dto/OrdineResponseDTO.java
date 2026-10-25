package it.mansville.euristest.dto;

import java.util.Set;

public record OrdineResponseDTO(
        Integer id,
        Integer idCliente,
        String nomeCliente,
        String cognomeCliente,
        String emailCliente,
        Set<DettaglioOrdineResponseDTO> dettagli,
        String stato
) {}