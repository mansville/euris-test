package it.mansville.euristest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DETTAGLIO_ORDINI")
@SuppressWarnings("JpaDataSourceORMInspection")
public class DettaglioOrdine {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_ORDINE")
    private Ordine ordine;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO")
    private Prodotto prodotto;

    @Column(name = "QUANTITA")
    private Integer quantita;
}