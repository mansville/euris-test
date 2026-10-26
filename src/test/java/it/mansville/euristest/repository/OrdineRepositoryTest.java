package it.mansville.euristest.repository;

import it.mansville.euristest.model.Cliente;
import it.mansville.euristest.model.DettaglioOrdine;
import it.mansville.euristest.model.Ordine;
import it.mansville.euristest.model.Prodotto;
import it.mansville.euristest.model.type.StatoOrdine;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrdineRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Test
    void testCreateAndFind() {
        var cliente = new Cliente();
        cliente.setNome("Manuel");
        cliente.setCognome("Nucci");
        cliente.setDataNascita(LocalDate.of(1993, 12, 26));
        cliente.setCodiceFiscale("NCCMNL93T26A944D");
        cliente.setEmail("manuel.nucci.52@gmail.com");
        var clienteSaved = clienteRepository.save(cliente);

        var prodotto = new Prodotto();
        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(10);
        var prodottoSaved = prodottoRepository.save(prodotto);

        var dettaglio = new DettaglioOrdine();
        dettaglio.setProdotto(prodottoSaved);
        dettaglio.setQuantita(5);

        var ordine = new Ordine();
        ordine.setCliente(clienteSaved);
        ordine.addDettaglio(dettaglio);

        var ordineSaved = ordineRepository.save(ordine);
        assertThat(ordineSaved.getId()).isNotNull();

        var ordini = ordineRepository.findByClienteCodiceFiscale("NCCMNL93T26A944D", Pageable.unpaged());
        assertThat(ordini.isEmpty()).isFalse();

        var ordinePerStato = ordineRepository.countByStato(StatoOrdine.CONSEGNATO);
        assertThat(ordinePerStato).isEqualTo(0);
    }
}