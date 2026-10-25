package it.mansville.euristest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mansville.euristest.dto.ProdottoRequestDTO;
import it.mansville.euristest.dto.ProdottoResponseDTO;
import it.mansville.euristest.mapper.ProdottoMapper;
import it.mansville.euristest.service.ProdottoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/prodotti")
@Tag(name = "Prodotti", description = "API REST per l'aggiunta, la ricerca e la gestione dello stock dei prodotti")
public class ProdottoController {
    private final ProdottoMapper prodottoMapper;

    private final ProdottoService prodottoService;

    @PostMapping
    @Operation(summary = "Aggiunge un nuovo prodotto, restituendo il suo id in caso di successo")
    public ResponseEntity<Integer> crea(@RequestBody @Valid ProdottoRequestDTO prodottoDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                prodottoService.crea(prodottoMapper.toEntity(prodottoDTO)));
    }

    @GetMapping
    @Operation(summary = "Cerca tutti i prodotti, restituendo una lista paginata")
    public ResponseEntity<Page<ProdottoResponseDTO>> lista(Pageable pageable) {
        return ResponseEntity.ok(prodottoService.lista(pageable).map(prodottoMapper::toDTO));
    }

    @GetMapping("/{codice}")
    @Operation(summary = "Cerca un prodotto con uno specifico codice")
    public ResponseEntity<ProdottoResponseDTO> cercaPerCodice(
            @PathVariable @NotBlank String codice) {
        var prodotto = prodottoService.cercaPerCodice(codice);

        if (prodotto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(prodottoMapper.toDTO(prodotto));
    }

    @PatchMapping("/{codice}")
    @Operation(summary = "Tenta l'aggiornamento dello stock di un prodotto con uno specifico codice, restituendo un riepilogo della situazione aggiornata")
    public ResponseEntity<ProdottoResponseDTO> aggiornaStock(
            @PathVariable @NotBlank String codice,
            @RequestParam @NotNull Integer variazione) {
        return ResponseEntity.ok(prodottoMapper.toDTO(prodottoService.aggiornaStock(codice, variazione)));
    }
}