package it.mansville.euristest.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "CLIENTI")
@SuppressWarnings("JpaDataSourceORMInspection")
public class Cliente {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Version
    @Column(name = "VERSION")
    private Integer version;

    @Column(name = "NOME")
    private String nome;

    @Column(name = "COGNOME")
    private String cognome;

    @Column(name = "DATA_NASCITA")
    private LocalDate dataNascita;

    @Column(name = "CODICE_FISCALE")
    private String codiceFiscale;

    @Column(name = "EMAIL")
    private String email;
}