package it.mansville.euristest.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record ClienteRequestDTO(
        @NotBlank
        String nome,

        @NotBlank
        String cognome,

        @Past
        @NotNull
        LocalDate dataNascita,

        @NotBlank
        @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
        String codiceFiscale,

        @NotBlank
        @Email
        String email
) {}