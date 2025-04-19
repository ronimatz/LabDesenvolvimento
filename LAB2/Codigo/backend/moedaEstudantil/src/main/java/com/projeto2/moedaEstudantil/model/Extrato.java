package com.projeto2.moedaEstudantil.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "extrato")
@Getter
@Setter
public class Extrato {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    private Integer saldoMoedas;

    @OneToOne
    private Usuario usuario;

    @OneToMany
    private List<Transacoes> transacoes;
}
