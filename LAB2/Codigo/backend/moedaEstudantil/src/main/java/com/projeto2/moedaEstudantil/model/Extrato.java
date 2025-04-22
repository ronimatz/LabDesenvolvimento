package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @JoinColumn(name = "usuario_id", referencedColumnName = "id")
    private Usuario usuario;

    @OneToMany
    private List<Transacoes> transacoes = new ArrayList<>();


    public Extrato() {
    }


    public Extrato(Usuario usuario) {
    
        this.usuario = usuario;
        
    }


}
