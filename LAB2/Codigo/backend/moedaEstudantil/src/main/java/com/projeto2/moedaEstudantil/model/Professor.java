package com.projeto2.moedaEstudantil.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
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
    
    @Column(unique = true, nullable = false)
    private String name;
    @Column(unique = true, nullable = false)
    private String cpf;

    @OneToOne
    @Column(nullable = false)
    private Departamento departamento;

    @OneToOne
    @Column(nullable = false)
    private InstituicaoEnsino instituicaoEnsino;

    @OneToOne
    @Column(nullable = false)
    private Extrato extrato;

}
