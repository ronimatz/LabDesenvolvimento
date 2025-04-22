package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
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
    @JoinColumn(name = "instituicao_ensino_id", nullable = false) 
    private InstituicaoEnsino instituicaoEnsino;

    @OneToOne
    @JoinColumn(name = "curso_id", nullable = false) 
    private Curso curso;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Extrato extrato;

    @OneToMany
    private List<Vantagem> vantagensAdquiridas;

    public Aluno(String email, String senha, String nome, String cpf, String rg, Endereco endereco, InstituicaoEnsino instituicaoEnsino, Curso curso) {
        super(email, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
        this.endereco = endereco;
        this.instituicaoEnsino = instituicaoEnsino;
        this.curso = curso;
        this.vantagensAdquiridas = new ArrayList<>();
    }
}
