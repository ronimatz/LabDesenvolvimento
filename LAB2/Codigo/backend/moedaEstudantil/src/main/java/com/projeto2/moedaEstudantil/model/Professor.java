package com.projeto2.moedaEstudantil.model;


import com.projeto2.moedaEstudantil.validation.CpfValido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
public class Professor extends Usuario {
    
    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    @CpfValido(message = "CPF inválido")
    private String cpf;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "instituicao_ensino_id", nullable = false)
    private InstituicaoEnsino instituicaoEnsino;

    private Integer saldoMoedas = 100; // Cada professor começa com 100 moedas

    public Professor(String email, String senha, String nome, String cpf, Departamento departamento, InstituicaoEnsino instituicaoEnsino) {
        super(email, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.departamento = departamento;
        this.instituicaoEnsino = instituicaoEnsino;
    }

}
