package it.mansville.euristest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mansville.euristest.dto.DettaglioOrdineRequestDTO;
import it.mansville.euristest.dto.OrdineRequestDTO;
import it.mansville.euristest.dto.OrdineResponseDTO;
import it.mansville.euristest.mapper.OrdineMapper;
import it.mansville.euristest.model.type.StatoOrdine;
import it.mansville.euristest.service.OrdineService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ordini")
@Tag(name = "Ordini", description = "API REST per la creazione, la ricerca, la consegna e la cancellazione degli ordini")
public class OrdineController {
    private final OrdineMapper ordineMapper;

    private final OrdineService ordineService;

    @PostMapping
    @Operation(summary = "Crea un nuovo ordine, restituendo il suo id in caso di successo e aggiornando lo stock dei prodotti richiesti")
    public ResponseEntity<Integer> crea(@RequestBody @Valid OrdineRequestDTO ordineDTO) {
        var dettagli = ordineDTO.dettagli()
                .stream().collect(Collectors.toMap(
                        DettaglioOrdineRequestDTO::codice,
                        DettaglioOrdineRequestDTO::quantita,
                        Integer::sum
                ));

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ordineService.crea(ordineDTO.codiceFiscale(), dettagli));
    }

    @GetMapping
    @Operation(summary = "Cerca tutti gli ordini, filtrandoli all'occorrenza per codice fiscale del cliente e per stato dell'ordine, restituendo una lista paginata")
    public ResponseEntity<Page<OrdineResponseDTO>> lista(
            @RequestParam(required = false) @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
            String codiceFiscale,
            @RequestParam(required = false)
            StatoOrdine stato,
            Pageable pageable) {
        return ResponseEntity.ok(ordineService.listaConFiltro(codiceFiscale, stato, pageable).map(ordineMapper::toDTO));
    }

    @GetMapping("/da-consegnare")
    @Operation(summary = "Restituisce il totale di tutti gli ordini eseguiti ma non ancora consegnati")
    public ResponseEntity<Integer> contaDaConsegnare() {
        return ResponseEntity.ok(ordineService.totaleDaConsegnare());
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Cambia lo stato di uno specifico ordine, impostandolo come consegnato")
    public ResponseEntity<Void> consegna(@PathVariable @Positive Integer id) {
        ordineService.consegna(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Tenta la cancellazione di un ordine ancora non consegnato, reimpostando gli stock dei relativi prodotti")
    public ResponseEntity<Void> delete(@PathVariable @Positive Integer id) {
        ordineService.cancella(id);

        return ResponseEntity.noContent().build();
    }
}