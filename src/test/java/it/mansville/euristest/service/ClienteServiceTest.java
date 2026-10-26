package it.mansville.euristest.service;

import it.mansville.euristest.model.Cliente;
import it.mansville.euristest.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void testCreateAndFind() {
        var cliente = new Cliente();
        cliente.setId(1);
        cliente.setNome("Manuel");
        cliente.setCognome("Nucci");
        cliente.setDataNascita(LocalDate.of(1993, 12, 26));
        cliente.setCodiceFiscale("NCCMNL93T26A944D");
        cliente.setEmail("manuel.nucci.52@gmail.com");

        when(clienteRepository.existsByCodiceFiscale("NCCMNL93T26A944D")).thenReturn(false);
        when(clienteRepository.existsByEmail("manuel.nucci.52@gmail.com")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        var clienteSaved = clienteService.crea(cliente);
        assertThat(clienteSaved).isNotNull();
        verify(clienteRepository).existsByCodiceFiscale("NCCMNL93T26A944D");
        verify(clienteRepository).existsByEmail("manuel.nucci.52@gmail.com");
        verify(clienteRepository).save(cliente);

        when(clienteRepository.findByCodiceFiscale("NCCMNL93T26A944D")).thenReturn(Optional.of(cliente));
        var clientePerCodiceFiscale = clienteService.cercaPerCodiceFiscale("NCCMNL93T26A944D");
        assertThat(clientePerCodiceFiscale).isNotNull();
        verify(clienteRepository).findByCodiceFiscale("NCCMNL93T26A944D");
    }
}