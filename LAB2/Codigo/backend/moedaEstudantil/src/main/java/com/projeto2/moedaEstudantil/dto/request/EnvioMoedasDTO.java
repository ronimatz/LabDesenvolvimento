package com.projeto2.moedaEstudantil.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnvioMoedasDTO {
    
    @NotNull(message = "O ID do aluno é obrigatório")
    private Integer alunoId;

    @NotNull(message = "A quantidade é obrigatória")
    @Min(value = 1, message = "A quantidade deve ser maior que zero")
    private Integer quantidade;

    @NotBlank(message = "O motivo é obrigatório")
    private String motivo;
} 