package it.mansville.euristest.service;

import it.mansville.euristest.model.Cliente;
import it.mansville.euristest.model.DettaglioOrdine;
import it.mansville.euristest.model.Ordine;
import it.mansville.euristest.model.Prodotto;
import it.mansville.euristest.model.type.StatoOrdine;
import it.mansville.euristest.repository.OrdineRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrdineServiceTest {
    @Mock
    private ClienteService clienteService;

    @Mock
    private ProdottoService prodottoService;

    @Mock
    private OrdineRepository ordineRepository;

    @InjectMocks
    private OrdineService ordineService;

    @Test
    void testCreateAndDelete() {
        var cliente = new Cliente();
        cliente.setNome("Manuel");
        cliente.setCognome("Nucci");
        cliente.setDataNascita(LocalDate.of(1993, 12, 26));
        cliente.setCodiceFiscale("NCCMNL93T26A944D");
        cliente.setEmail("manuel.nucci.52@gmail.com");

        var prodotto = new Prodotto();
        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(5);

        var dettaglio = new DettaglioOrdine();
        dettaglio.setProdotto(prodotto);
        dettaglio.setQuantita(5);

        var ordine = new Ordine();
        ordine.setId(1);
        ordine.setCliente(cliente);
        ordine.addDettaglio(dettaglio);

        when(clienteService.cercaPerCodiceFiscale("NCCMNL93T26A944D")).thenReturn(cliente);
        when(prodottoService.aggiornaStock("PROD-001", -5)).thenReturn(prodotto);
        when(ordineRepository.save(any(Ordine.class))).thenReturn(ordine);
        var ordineSaved = ordineService.crea("NCCMNL93T26A944D", Map.of("PROD-001", 5));
        assertThat(ordineSaved).isNotNull();
        verify(clienteService).cercaPerCodiceFiscale("NCCMNL93T26A944D");
        verify(prodottoService).aggiornaStock("PROD-001", -5);
        verify(ordineRepository).save(any(Ordine.class));

        clearInvocations(prodottoService);
        ordine.setStato(StatoOrdine.CONSEGNATO);
        when(ordineRepository.findById(1)).thenReturn(Optional.of(ordine));
        assertThatThrownBy(() -> ordineService.cancella(1))
                .isInstanceOf(IllegalArgumentException.class);
        verify(ordineRepository).findById(1);
        verify(ordineRepository, never()).delete(any());
        verifyNoInteractions(prodottoService);
    }
}