package it.mansville.euristest.repository;

import it.mansville.euristest.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    boolean existsByCodiceFiscale(String codiceFiscale);

    boolean existsByEmail(String email);

    Optional<Cliente> findByCodiceFiscale(String codiceFiscale);

    Optional<Cliente> findByEmail(String email);
}