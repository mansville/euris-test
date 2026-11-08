package it.mansville.euristest.command;

import java.util.Map;

public record CreaOrdineCommand(
        String codiceFiscale,
        Map<String, Integer> dettagli
) {}