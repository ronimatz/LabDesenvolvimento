package com.projeto2.moedaEstudantil.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VantagemDTO {
    private Integer id;
    private String descricao;
    private Double valor;
    private Double desconto;
    private String fotoProduto;
}