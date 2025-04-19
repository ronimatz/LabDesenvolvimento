package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "instituicoes_ensino")
@Getter
@Setter
@NoArgsConstructor
public class InstituicaoEnsino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "instituicaoEnsino", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Professor> professores;

    @OneToMany(mappedBy = "instituicaoEnsino", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Departamento> departamentos;

    public InstituicaoEnsino(String nome) {
        this.nome = nome;
        this.professores = new ArrayList<>();
        this.departamentos = new ArrayList<>();
    }

    public void adicionarProfessor(Professor professor) {
        professor.setInstituicaoEnsino(this);
        this.professores.add(professor);
    }

    public void adicionarDepartamento(Departamento departamento) {
        departamento.setInstituicaoEnsino(this);
        this.departamentos.add(departamento);
    }

}
