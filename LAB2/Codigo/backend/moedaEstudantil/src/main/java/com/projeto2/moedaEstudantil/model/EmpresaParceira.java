package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empresas_parceiras")
@Getter
@Setter
@NoArgsConstructor
public class EmpresaParceira extends Usuario {
    
    @Column(unique = true, nullable = false)
    private String nome;

    @OneToMany
    private List<Vantagem> vantagens = new ArrayList<>();

    @Column(unique = true, nullable = false)
    private String cnpj;

    public EmpresaParceira(String email, String senha, String nome, String cnpj) {
        super(email, senha);
        this.nome = nome;
        this.cnpj = cnpj;
        this.vantagens = new ArrayList<>();
    }
}
