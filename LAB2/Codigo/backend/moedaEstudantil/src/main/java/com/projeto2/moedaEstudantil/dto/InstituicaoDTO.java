package com.projeto2.moedaEstudantil.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InstituicaoDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    private String cnpj;
} 