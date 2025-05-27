package com.projeto2.moedaEstudantil.dto.response;

import lombok.Data;

@Data
public class AlunoInfoDTO {
    private Integer id;
    private String nome;
    private String email;
    private Double saldoMoedas;
    private String curso;
    private String instituicao;
} 