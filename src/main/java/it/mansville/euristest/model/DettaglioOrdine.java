package it.mansville.euristest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "DETTAGLIO_ORDINI")
@SuppressWarnings("JpaDataSourceORMInspection")
public class DettaglioOrdine {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "ID_ORDINE")
    private Ordine ordine;

    @ManyToOne
    @JoinColumn(name = "ID_PRODOTTO")
    private Prodotto prodotto;

    @Column(name = "QUANTITA")
    private Integer quantita;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DettaglioOrdine other)) {
            return false;
        }

        return Objects.equals(ordine, other.ordine) && Objects.equals(prodotto, other.prodotto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ordine, prodotto);
    }
}