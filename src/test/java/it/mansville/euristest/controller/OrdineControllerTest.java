package it.mansville.euristest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mansville.euristest.dto.DettaglioOrdineRequestDTO;
import it.mansville.euristest.dto.OrdineRequestDTO;
import it.mansville.euristest.model.Cliente;
import it.mansville.euristest.model.Prodotto;
import it.mansville.euristest.model.type.StatoOrdine;
import it.mansville.euristest.repository.ClienteRepository;
import it.mansville.euristest.repository.OrdineRepository;
import it.mansville.euristest.repository.ProdottoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class OrdineControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    private void creaCliente() {
        var cliente = new Cliente();

        cliente.setNome("Manuel");
        cliente.setCognome("Nucci");
        cliente.setDataNascita(LocalDate.of(1993, 12, 26));
        cliente.setCodiceFiscale("NCCMNL93T26A944D");
        cliente.setEmail("manuel.nucci.52@gmail.com");

        clienteRepository.save(cliente);
    }

    private void creaProdotto() {
        var prodotto = new Prodotto();

        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(10);

        prodottoRepository.save(prodotto);
    }

    @Test
    void crea() throws Exception {
        creaCliente();
        creaProdotto();

        var request = new OrdineRequestDTO(
                "NCCMNL93T26A944D",
                List.of(new DettaglioOrdineRequestDTO("PROD-001", 15))
        );

        mockMvc.perform(post("/api/ordini")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        var prodotto = prodottoRepository.findByCodice("PROD-001").orElseThrow();
        assertThat(prodotto.getStock()).isEqualTo(10);
    }

    @Test
    void lista() throws Exception {
        creaCliente();
        creaProdotto();

        mockMvc.perform(post("/api/ordini")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OrdineRequestDTO("NCCMNL93T26A944D", List.of(new DettaglioOrdineRequestDTO("PROD-001", 5))))))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/ordini")
                .param("codiceFiscale", "NCCMNL93T26A944D"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    void contaDaConsegnare() throws Exception {
        creaCliente();
        creaProdotto();

        var totaleString = mockMvc.perform(get("/api/ordini/da-consegnare"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        var totale = Integer.parseInt(totaleString);

        mockMvc.perform(post("/api/ordini")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OrdineRequestDTO("NCCMNL93T26A944D", List.of(new DettaglioOrdineRequestDTO("PROD-001", 5))))))
                .andExpect(status().isCreated());
        mockMvc.perform(get("/api/ordini/da-consegnare"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(totale + 1)));
    }

    @Test
    void consegna() throws Exception {
        creaCliente();
        creaProdotto();

        var result = mockMvc.perform(post("/api/ordini")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OrdineRequestDTO("NCCMNL93T26A944D", List.of(new DettaglioOrdineRequestDTO("PROD-001", 5))))))
                .andExpect(status().isCreated())
                .andReturn();
        var id = Integer.parseInt(result.getResponse().getContentAsString());

        mockMvc.perform(patch("/api/ordini/{id}", id))
                .andExpect(status().isNoContent());

        var ordine = ordineRepository.findById(id).orElseThrow();
        assertThat(ordine.getStato()).isEqualTo(StatoOrdine.CONSEGNATO);
    }

    @Test
    void cancella() throws Exception {
        creaCliente();
        creaProdotto();

        var result = mockMvc.perform(post("/api/ordini")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new OrdineRequestDTO("NCCMNL93T26A944D", List.of(new DettaglioOrdineRequestDTO("PROD-001", 5))))))
                .andExpect(status().isCreated())
                .andReturn();
        var id = Integer.parseInt(result.getResponse().getContentAsString());

        var prodottoPrima = prodottoRepository.findByCodice("PROD-001").orElseThrow();
        assertThat(prodottoPrima.getStock()).isEqualTo(5);

        mockMvc.perform(delete("/api/ordini/{id}", id))
                .andExpect(status().isNoContent());

        assertThat(ordineRepository.findById(id)).isEmpty();

        var prodottoDopo = prodottoRepository.findByCodice("PROD-001").orElseThrow();
        assertThat(prodottoDopo.getStock()).isEqualTo(10);
    }
}