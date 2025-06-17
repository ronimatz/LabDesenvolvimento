package com.projeto2.moedaEstudantil.model;

import com.projeto2.moedaEstudantil.validation.CpfValido;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "professores")
@Getter
@Setter
@NoArgsConstructor
public class Professor extends Usuario {
    
    @NotBlank(message = "O nome é obrigatório")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    @Column(unique = true, nullable = false)
    @CpfValido(message = "CPF inválido")
    private String cpf;

    @NotNull(message = "O departamento é obrigatório")
    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @NotNull(message = "A instituição de ensino é obrigatória")
    @ManyToOne
    @JoinColumn(name = "instituicao_ensino_id", nullable = false)
    private InstituicaoEnsino instituicaoEnsino;

    @NotNull(message = "O saldo de moedas é obrigatório")
    @Min(value = 0, message = "O saldo de moedas não pode ser negativo")
    private Integer saldoMoedas = 100; // Cada professor começa com 100 moedas

    public Professor(String email, String senha, String nome, String cpf, Departamento departamento, InstituicaoEnsino instituicaoEnsino) {
        super(email, senha);
        this.nome = nome;
        this.cpf = cpf;
        this.departamento = departamento;
        this.instituicaoEnsino = instituicaoEnsino;
    }

}
