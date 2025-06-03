package com.projeto2.moedaEstudantil.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DepartamentoDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "O ID da instituição é obrigatório")
    private Integer instituicaoId;
} 