package it.mansville.euristest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record OrdineRequestDTO(
        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
        String codiceFiscale,

        @NotEmpty
        List<@Valid DettaglioOrdineRequestDTO> dettagli
) {}