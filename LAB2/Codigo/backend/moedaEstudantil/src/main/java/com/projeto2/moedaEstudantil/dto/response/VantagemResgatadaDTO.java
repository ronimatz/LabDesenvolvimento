package com.projeto2.moedaEstudantil.dto.response;

import java.time.LocalDateTime;
import lombok.Data;

@Data
public class VantagemResgatadaDTO {
    private Integer id;
    private String vantagemDescricao;
    private String empresaNome;
    private String alunoNome;
    private Double valor;
    private LocalDateTime data;
    private String cupomGerado;
    private String imagemUrl;
} 