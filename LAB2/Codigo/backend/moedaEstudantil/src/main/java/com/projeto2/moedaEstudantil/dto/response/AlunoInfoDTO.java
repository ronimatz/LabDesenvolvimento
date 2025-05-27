package com.projeto2.moedaEstudantil.dto.response;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlunoInfoDTO {
    private String nome;
    private Integer saldoMoedas;
} 