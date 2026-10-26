package it.mansville.euristest.service;

import it.mansville.euristest.model.DettaglioOrdine;
import it.mansville.euristest.model.Ordine;
import it.mansville.euristest.model.type.StatoOrdine;
import it.mansville.euristest.repository.OrdineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrdineService {
    private final OrdineRepository ordineRepository;

    private final ClienteService clienteService;

    private final ProdottoService prodottoService;

    @Transactional
    public Integer crea(String codiceFiscale, Map<String, Integer> codiciQuantita) {
        var ordine = new Ordine();
        var cliente = clienteService.cercaPerCodiceFiscale(codiceFiscale);

        if (cliente == null) {
            throw new IllegalArgumentException("Creazione Ordine fallito - Cliente con CF %s non trovato"
                    .formatted(codiceFiscale));
        }

        ordine.setCliente(cliente);

        for (var cq : codiciQuantita.entrySet()) {
            var dettaglio = new DettaglioOrdine();
            var prodotto = prodottoService.aggiornaStock(cq.getKey(), -cq.getValue());

            dettaglio.setProdotto(prodotto);
            dettaglio.setQuantita(cq.getValue());
            ordine.addDettaglio(dettaglio);
        }

        return ordineRepository.save(ordine).getId();
    }

    public Page<Ordine> listaConFiltro(String codiceFiscale, StatoOrdine stato, Pageable pageable) {
        if (codiceFiscale == null || codiceFiscale.isBlank()) {
            return ordineRepository.findAll(pageable);
        }
        if (stato == null) {
            return ordineRepository.findByClienteCodiceFiscale(codiceFiscale, pageable);
        }

        return ordineRepository.findByClienteCodiceFiscaleAndStato(codiceFiscale, stato, pageable);
    }

    public int totaleDaConsegnare() {
        return ordineRepository.countByStato(StatoOrdine.ORDINATO);
    }

    @Transactional
    public void consegna(Integer ordineId) {
        var ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new IllegalArgumentException("Consegna fallita - Ordine %d non trovato"
                        .formatted(ordineId)));

        ordine.setStato(StatoOrdine.CONSEGNATO);
    }

    @Transactional
    public void cancella(Integer ordineId) {
        var ordine = ordineRepository.findById(ordineId)
                .orElseThrow(() -> new IllegalArgumentException("Cancellazione fallita - Ordine %d non trovato"
                        .formatted(ordineId)));

        if (StatoOrdine.CONSEGNATO.equals(ordine.getStato())) {
            throw new IllegalArgumentException("Cancellazione fallita - Ordine %d già consegnato"
                    .formatted(ordineId));
        }

        for (var dettaglio : ordine.getDettagli()) {
            prodottoService.aggiornaStock(dettaglio.getProdotto().getCodice(), dettaglio.getQuantita());
        }

        ordineRepository.delete(ordine);
    }
}