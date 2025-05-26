package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;


import com.validation.CpfValido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
public class Aluno extends Usuario {
    @Column(nullable = false)
    private String nome;
    
    @Column(unique = true, nullable = false)
   // @CPF(message = "CPF inválido")
    @CpfValido
    private String cpf;
    
    @Column(unique = true, nullable = false)
    private String rg;

    @Column(nullable = false)
    @Embedded
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "instituicao_ensino_id", nullable = false) 
    private InstituicaoEnsino instituicaoEnsino;

    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false) 
    private Curso curso;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
    private Extrato extrato;

    @OneToMany
    private List<Vantagem> vantagensAdquiridas;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private Integer saldoMoedas = 0; // Alunos começam com 0 moedas

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
