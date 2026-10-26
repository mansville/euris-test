package it.mansville.euristest.repository;

import it.mansville.euristest.model.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ClienteRepositoryTest {
    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void testCreateAndFind() {
        var cliente = new Cliente();
        cliente.setNome("Manuel");
        cliente.setCognome("Nucci");
        cliente.setDataNascita(LocalDate.of(1993, 12, 26));
        cliente.setCodiceFiscale("NCCMNL93T26A944D");
        cliente.setEmail("manuel.nucci.52@gmail.com");

        var clienteSaved = clienteRepository.save(cliente);
        assertThat(clienteSaved.getId()).isNotNull();

        var clientePerCodiceFiscale = clienteRepository.findByCodiceFiscale("NCCMNL93T26A944D");
        assertThat(clientePerCodiceFiscale).isPresent();

        var clientePerEmail = clienteRepository.findByEmail("manuel.nucci@gmail.com");
        assertThat(clientePerEmail).isNotPresent();
    }
}