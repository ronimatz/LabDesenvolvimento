package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;

import com.projeto2.moedaEstudantil.validation.CpfValido;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alunos")
@Getter
@Setter
@NoArgsConstructor
public class Aluno extends Usuario {
    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @NotBlank(message = "O CPF é obrigatório")
    @Column(unique = true, nullable = false)
    @CpfValido
    private String cpf;
    
    @NotBlank(message = "O RG é obrigatório")
    @Column(unique = true, nullable = false)
    private String rg;

    @NotNull(message = "O endereço é obrigatório")
    @Column(nullable = false)
    @Embedded
    private Endereco endereco;

    @NotNull(message = "A instituição de ensino é obrigatória")
    @ManyToOne
    @JoinColumn(name = "instituicao_ensino_id", nullable = false) 
    private InstituicaoEnsino instituicaoEnsino;

    @NotNull(message = "O curso é obrigatório")
    @ManyToOne
    @JoinColumn(name = "curso_id", nullable = false) 
    private Curso curso;


    @OneToMany
    private List<Vantagem> vantagensAdquiridas;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull(message = "O saldo de moedas é obrigatório")
    @Column(nullable = false)
    private Double saldoMoedas = 0.0; // Alunos começam com 0 moedas

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
