package it.mansville.euristest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.mansville.euristest.dto.ClienteRequestDTO;
import it.mansville.euristest.model.Cliente;
import it.mansville.euristest.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
class ClienteControllerTest {
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ClienteRepository clienteRepository;

    private void creaClienteSalvataggio() {
        var cliente = new Cliente();

        cliente.setNome("Manuel");
        cliente.setCognome("Nucci");
        cliente.setDataNascita(LocalDate.of(1993, 12, 26));
        cliente.setCodiceFiscale("NCCMNL93T26A944D");
        cliente.setEmail("manuel.nucci.52@gmail.com");

        clienteRepository.save(cliente);
    }

    @Test
    void crea() throws Exception {
        var request = new ClienteRequestDTO(
                "Manuel",
                "Nucci",
                LocalDate.of(1993, 12, 26),
                "NCCMNL93T26A944D",
                "manuel.nucci.52@gmail.com"
        );

        var result = mockMvc.perform(post("/api/clienti")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();

        var id = Integer.parseInt(result.getResponse().getContentAsString());
        assertThat(id).isPositive();

        var cliente = clienteRepository.findById(id);
        assertThat(cliente).isPresent();
    }

    @Test
    void lista() throws Exception {
        creaClienteSalvataggio();

        mockMvc.perform(get("/api/clienti")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(equalTo(1))));
    }

    @Test
    void cercaPerCodiceFiscale() throws Exception {
        creaClienteSalvataggio();

        mockMvc.perform(get("/api/clienti/codice-fiscale/{codiceFiscale}", "NCCMNL93T26A944D"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codiceFiscale", is("NCCMNL93T26A944D")));
    }

    @Test
    void cercaPerEmail() throws Exception {
        creaClienteSalvataggio();

        mockMvc.perform(get("/api/clienti/email/{email}", "manuel.nucci.52@gmail.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codiceFiscale", is("NCCMNL93T26A944D")));
    }
}