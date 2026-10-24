package it.mansville.euristest.service;

import it.mansville.euristest.model.Prodotto;
import it.mansville.euristest.repository.ProdottoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProdottoService {
    private final ProdottoRepository prodottoRepository;

    @Transactional
    public Integer crea(Prodotto prodotto) {
        if (prodottoRepository.existsByCodice(prodotto.getCodice())) {
            throw new IllegalArgumentException("Inserimento Prodotto fallito - Codice %s già utilizzato"
                    .formatted(prodotto.getCodice()));
        }

        try {
            return prodottoRepository.save(prodotto).getId();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Inserimento fallito - Prodotto già presente");
        }
    }

    public Page<Prodotto> lista(Pageable pageable) {
        return prodottoRepository.findAll(pageable);
    }

    public Prodotto cercaPerCodice(String codice) {
        return prodottoRepository.findByCodice(codice).orElse(null);
    }

    @Transactional
    public Prodotto aggiornaStock(String codice, Integer variazione) {
        var prodotto = prodottoRepository.findByCodiceWithLock(codice)
                .orElseThrow(() -> new IllegalArgumentException("Aggiornamento Prodotto fallito - Codice %s non trovato"
                        .formatted(codice)));

        var nuovoStock = prodotto.getStock() + variazione;

        if (nuovoStock < 0) {
            throw new IllegalArgumentException("Ordine non evadibile (Codice: %s - Stock: %d)"
                    .formatted(codice, prodotto.getStock()));
        }

        prodotto.setStock(nuovoStock);

        return prodotto;
    }
}