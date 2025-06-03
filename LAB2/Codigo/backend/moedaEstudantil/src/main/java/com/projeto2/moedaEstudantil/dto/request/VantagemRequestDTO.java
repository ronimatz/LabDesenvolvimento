package com.projeto2.moedaEstudantil.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VantagemRequestDTO {
    @NotBlank(message = "A descrição da vantagem é obrigatória")
    private String descricao;

    @NotNull(message = "O valor da vantagem é obrigatório")
    @Positive(message = "O valor da vantagem deve ser maior que zero")
    private Double valor;

    @NotNull(message = "O desconto da vantagem é obrigatório")
    @Positive(message = "O desconto da vantagem deve ser maior que zero")
    private Double desconto;

    @NotBlank(message = "A foto da vantagem é obrigatória")
    private String fotoVantagem;
} 