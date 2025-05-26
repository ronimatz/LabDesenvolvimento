package com.projeto2.moedaEstudantil.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CadastroCursoDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "O ID do departamento é obrigatório")
    private Integer departamentoId;
} 