package it.mansville.euristest.dto;

import java.time.LocalDate;

public record ClienteResponseDTO(
        Integer id,
        String nome,
        String cognome,
        LocalDate dataNascita,
        String codiceFiscale,
        String email
) {}