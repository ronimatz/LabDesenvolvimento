package com.projeto2.moedaEstudantil.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CadastroCursoDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "O ID do departamento é obrigatório")
    private Integer departamentoId;
    
    @NotNull(message = "A carga horária é obrigatória")
    @Positive(message = "A carga horária deve ser um número positivo")
    private Integer cargaHoraria;
} 