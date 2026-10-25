package it.mansville.euristest.model;

import it.mansville.euristest.model.type.StatoOrdine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "ORDINI")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("JpaDataSourceORMInspection")
public class Ordine {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @ManyToOne
    @JoinColumn(name = "ID_CLIENTE")
    private Cliente cliente;

    @OneToMany(mappedBy = "ordine", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private Set<DettaglioOrdine> dettagli = new HashSet<>();

    @Column(name = "STATO")
    @Enumerated(EnumType.STRING)
    private StatoOrdine stato = StatoOrdine.ORDINATO;

    @CreatedDate
    @Column(name = "DATA_CREAZIONE")
    private LocalDateTime dataCreazione;

    @LastModifiedDate
    @Column(name = "DATA_MODIFICA")
    private LocalDateTime dataModifica;

    public void addDettaglio(DettaglioOrdine dettaglio) {
        dettaglio.setOrdine(this);
        dettagli.add(dettaglio);
    }
}