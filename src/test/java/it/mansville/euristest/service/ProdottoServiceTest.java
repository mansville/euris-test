package it.mansville.euristest.service;

import it.mansville.euristest.model.Prodotto;
import it.mansville.euristest.repository.ProdottoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdottoServiceTest {
    @Mock
    private ProdottoRepository prodottoRepository;

    @InjectMocks
    private ProdottoService prodottoService;

    @Test
    void testCreate() {
        var prodotto = new Prodotto();
        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(10);

        when(prodottoRepository.save(any(Prodotto.class))).thenReturn(prodotto);

        var prodottoSaved = prodottoService.crea(prodotto);
        assertThat(prodottoSaved).isNotNull();

        verify(prodottoRepository).save(prodotto);
    }

    @Test
    void testFind() {
        var prodotto = new Prodotto();
        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(10);

        when(prodottoRepository.findByCodice("PROD-001")).thenReturn(Optional.of(prodotto));

        var prodottoPerCodice = prodottoService.cercaPerCodice("PROD-001");
        assertThat(prodottoPerCodice).isNotNull();

        verify(prodottoRepository).findByCodice("PROD-001");
    }

    @Test
    void testUpdate() {
        var prodotto = new Prodotto();
        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(10);

        when(prodottoRepository.findByCodiceWithLock("PROD-001")).thenReturn(Optional.of(prodotto));

        assertThatThrownBy(() ->
                prodottoService.aggiornaStock("PROD-001", -15))
                .isInstanceOf(IllegalArgumentException.class);

        verify(prodottoRepository).findByCodiceWithLock("PROD-001");
    }
}