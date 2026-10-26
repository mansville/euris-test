package it.mansville.euristest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mansville.euristest.dto.ProdottoRequestDTO;
import it.mansville.euristest.model.Prodotto;
import it.mansville.euristest.repository.ProdottoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ProdottoControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProdottoRepository prodottoRepository;

    private void creaProdottoSalvataggio() {
        var prodotto = new Prodotto();

        prodotto.setCodice("PROD-001");
        prodotto.setNome("HP Notebook");
        prodotto.setStock(10);

        prodottoRepository.save(prodotto);
    }

    @Test
    void crea() throws Exception {
        var request = new ProdottoRequestDTO("PROD-001", "HP Notebook", 10);

        var result = mockMvc.perform(post("/api/prodotti")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        var id = Integer.parseInt(result.getResponse().getContentAsString());
        assertThat(id).isPositive();

        var prodotto = prodottoRepository.findById(id);
        assertThat(prodotto).isPresent();
    }

    @Test
    void lista() throws Exception {
        creaProdottoSalvataggio();

        mockMvc.perform(get("/api/prodotti")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))));
    }

    @Test
    void cercaPerCodice() throws Exception {
        creaProdottoSalvataggio();

        mockMvc.perform(get("/api/prodotti/{codice}", "PROD-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codice", is("PROD-001")));
    }

    @Test
    void aggiornaStock() throws Exception {
        creaProdottoSalvataggio();

        mockMvc.perform(patch("/api/prodotti/{codice}", "PROD-001")
                .param("variazione", "-15"))
                .andExpect(status().isConflict());

        var prodotto = prodottoRepository.findByCodice("PROD-001").orElseThrow();
        assertThat(prodotto.getStock()).isEqualTo(10);
    }
}