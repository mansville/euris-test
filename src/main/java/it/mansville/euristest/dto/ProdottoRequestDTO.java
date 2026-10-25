package it.mansville.euristest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProdottoRequestDTO(
        @NotBlank
        String codice,

        @NotBlank
        String nome,

        @NotNull
        @Positive
        Integer stock
) {}