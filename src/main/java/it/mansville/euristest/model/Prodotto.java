package it.mansville.euristest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "PRODOTTI")
@EntityListeners(AuditingEntityListener.class)
@SuppressWarnings("JpaDataSourceORMInspection")
public class Prodotto {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "CODICE")
    private String codice;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "STOCK")
    private Integer stock;

    @LastModifiedDate
    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;
}