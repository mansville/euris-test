package it.mansville.euristest.service;

import it.mansville.euristest.model.Cliente;
import it.mansville.euristest.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClienteService {
    private final ClienteRepository clienteRepository;

    @Transactional
    public Integer crea(Cliente cliente) {
        if (clienteRepository.existsByCodiceFiscale(cliente.getCodiceFiscale())) {
            throw new IllegalArgumentException("Inserimento Cliente fallito - Codice Fiscale %s già utilizzato"
                    .formatted(cliente.getCodiceFiscale()));
        }
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new IllegalArgumentException("Inserimento Cliente fallito - Email %s già utilizzata"
                    .formatted(cliente.getEmail()));
        }

        try {
            return clienteRepository.save(cliente).getId();
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Inserimento fallito - Cliente già presente");
        }
    }

    public Page<Cliente> lista(Pageable pageable) {
        return clienteRepository.findAll(pageable);
    }

    public Cliente cercaPerCodiceFiscale(String codiceFiscale) {
        return clienteRepository.findByCodiceFiscale(codiceFiscale).orElse(null);
    }

    public Cliente cercaPerEmail(String email) {
        return clienteRepository.findByEmail(email).orElse(null);
    }
}