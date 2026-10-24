package it.mansville.euristest.repository;

import it.mansville.euristest.model.Ordine;
import it.mansville.euristest.model.type.StatoOrdine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Integer> {
    Page<Ordine> findByClienteId(Integer clienteId, Pageable pageable);

    Page<Ordine> findByClienteIdAndStato(Integer clienteId, StatoOrdine stato, Pageable pageable);

    int countByStato(StatoOrdine stato);
}