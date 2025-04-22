package com.projeto2.moedaEstudantil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CursoDTO {
    private String nome;
    private Integer cargaHoraria;
    private Integer departamentoId; 
}