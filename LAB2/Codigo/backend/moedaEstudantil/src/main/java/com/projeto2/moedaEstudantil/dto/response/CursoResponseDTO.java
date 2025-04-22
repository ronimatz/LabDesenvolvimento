package com.projeto2.moedaEstudantil.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CursoResponseDTO {
    private Integer id;
    private String nome;
    private Integer cargaHoraria;
    private String departamentoNome;
}