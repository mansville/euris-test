package it.mansville.euristest.repository;

import it.mansville.euristest.model.Prodotto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProdottoRepositoryTest {
    @Autowired
    private ProdottoRepository prodottoRepository;

    @Test
    void testCreateAndFind() {
        var prodotto = new Prodotto();
        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(10);

        var prodottoSaved = prodottoRepository.save(prodotto);
        assertThat(prodottoSaved.getId()).isNotNull();

        var prodottoByCodice = prodottoRepository.findByCodice("PROD-001");
        assertThat(prodottoByCodice).isPresent();
        assertThat(prodottoByCodice.get().getDataModifica()).isNotNull();
    }
}