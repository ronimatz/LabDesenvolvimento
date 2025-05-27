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
public class TransacaoRecebimentoDTO {
    private String professorNome;
    private LocalDateTime data;
    private String motivo;
    private Integer quantidade;
} 