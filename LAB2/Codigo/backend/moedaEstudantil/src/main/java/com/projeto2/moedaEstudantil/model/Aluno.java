package com.projeto2.moedaEstudantil.model;

import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alunos")
@Getter
@Setter
public class Aluno extends Usuario {
    @Column(nullable = false)
    private String nome;
    @Column(unique = true, nullable = false)
    @CPF(message = "CPF inválido")
    private String cpf;
    @Column(unique = true, nullable = false)
    private String rg;

    @Column(nullable = false)
    @Embedded
    private Endereco endereco;

    @OneToOne
    @Column(nullable = false)
    private InstituicaoEnsino instituicaoEnsino;

    @OneToOne
    @Column(nullable = false)
    private Curso curso;

    @OneToOne
    @Column(nullable = false)
    private Extrato extrato;

    @OneToMany
    private List<Vantagem> vantagensAdquiridas;
    

}
