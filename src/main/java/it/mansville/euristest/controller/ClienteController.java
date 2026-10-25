package it.mansville.euristest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.mansville.euristest.dto.ClienteRequestDTO;
import it.mansville.euristest.dto.ClienteResponseDTO;
import it.mansville.euristest.mapper.ClienteMapper;
import it.mansville.euristest.service.ClienteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@RequestMapping("/api/clienti")
@Tag(name = "Clienti", description = "API REST per l'aggiunta e la ricerca dei clienti")
public class ClienteController {
    private final ClienteMapper clienteMapper;

    private final ClienteService clienteService;

    @PostMapping
    @Operation(summary = "Aggiunge un nuovo cliente, restituendo il suo id in caso di successo")
    public ResponseEntity<Integer> crea(@RequestBody @Valid ClienteRequestDTO clienteDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                clienteService.crea(clienteMapper.toEntity(clienteDTO)));
    }

    @GetMapping
    @Operation(summary = "Cerca tutti i clienti, restituendo una lista paginata")
    public ResponseEntity<Page<ClienteResponseDTO>> lista(Pageable pageable) {
        return ResponseEntity.ok(clienteService.lista(pageable).map(clienteMapper::toDTO));
    }

    @GetMapping("/codice-fiscale/{codiceFiscale}")
    @Operation(summary = "Cerca un cliente con uno specifico codice fiscale")
    public ResponseEntity<ClienteResponseDTO> cercaPerCodiceFiscale(
            @PathVariable @NotBlank
            @Pattern(regexp = "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$")
            String codiceFiscale) {
        var cliente = clienteService.cercaPerCodiceFiscale(codiceFiscale);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clienteMapper.toDTO(cliente));
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Cerca un cliente con uno specifico indirizzo email")
    public ResponseEntity<ClienteResponseDTO> cercaPerEmail(
            @PathVariable @NotBlank @Email
            String email) {
        var cliente = clienteService.cercaPerEmail(email);

        if (cliente == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(clienteMapper.toDTO(cliente));
    }
}