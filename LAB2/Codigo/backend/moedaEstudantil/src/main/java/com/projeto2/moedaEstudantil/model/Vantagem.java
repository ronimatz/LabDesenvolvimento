package com.projeto2.moedaEstudantil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vantagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String descricao;
    @Column(nullable = false)
    private Double valor;

    private byte[] fotoProduto;
    @Column(nullable = false)
    private Double desconto;

    @ManyToOne
    @JoinColumn(name = "empresa_parceira_id") 
    @JsonIgnore
    private EmpresaParceira empresaParceiraResponsavel;

    public Vantagem() {
    }

    public Vantagem(Integer id, String descricao, Double valor, byte[] fotoProduto, Double desconto, EmpresaParceira empresaParceiraResponsavel) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.fotoProduto = fotoProduto;
        this.desconto = desconto;
        this.empresaParceiraResponsavel = empresaParceiraResponsavel;
    }


}
