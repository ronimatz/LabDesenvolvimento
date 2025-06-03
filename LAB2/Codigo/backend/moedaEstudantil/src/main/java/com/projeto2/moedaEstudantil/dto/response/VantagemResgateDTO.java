package com.projeto2.moedaEstudantil.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VantagemResgateDTO {
    private String descricao;
    private Double valor;
    private Double desconto;
    private String empresaNome;
    private String cupomGerado;
    private Double saldoAposResgate;
} 