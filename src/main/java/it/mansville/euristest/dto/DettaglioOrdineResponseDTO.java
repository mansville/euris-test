package it.mansville.euristest.dto;

public record DettaglioOrdineResponseDTO(
        Integer idProdotto,
        String codiceProdotto,
        String nomeProdotto,
        Integer quantita
) {}