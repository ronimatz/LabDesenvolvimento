package com.projeto2.moedaEstudantil.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue
    private Integer id;

    
    private String nome;
    private Integer cargaHoraria;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;
}
