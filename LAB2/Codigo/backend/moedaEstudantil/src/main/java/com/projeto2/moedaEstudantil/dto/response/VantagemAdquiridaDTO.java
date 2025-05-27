package com.projeto2.moedaEstudantil.dto.response;

import java.time.LocalDateTime;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VantagemAdquiridaDTO {
    private String empresa;
    private String descricao;
    private LocalDateTime dataAquisicao;
    private Integer custo;
    private String status;
} 