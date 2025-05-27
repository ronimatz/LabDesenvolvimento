package com.projeto2.moedaEstudantil.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransacaoRecebimentoDTO {
    private Integer id;
    private String professorNome;
    private LocalDateTime data;
    private String motivo;
    private Double valor;
} 