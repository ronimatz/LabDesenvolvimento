package com.projeto2.moedaEstudantil.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class TransacaoHistoricoDTO {
    private Integer id;
    private String alunoNome;
    private String alunoEmail;
    private Integer quantidade;
    private String motivo;
    private LocalDateTime data;
} 