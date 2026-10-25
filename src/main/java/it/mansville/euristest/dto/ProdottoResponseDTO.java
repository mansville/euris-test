package it.mansville.euristest.dto;

public record ProdottoResponseDTO(
        Integer id,
        String codice,
        String nome,
        Integer stock
) {}