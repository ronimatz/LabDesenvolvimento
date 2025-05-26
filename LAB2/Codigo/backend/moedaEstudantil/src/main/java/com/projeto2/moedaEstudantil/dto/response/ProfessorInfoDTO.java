package com.projeto2.moedaEstudantil.dto.response;

import lombok.Data;

@Data
public class ProfessorInfoDTO {
    private Integer id;
    private String nome;
    private String email;
    private Integer saldoMoedas;
    private DepartamentoInfo departamento;
    private InstituicaoInfo instituicaoEnsino;

    @Data
    public static class DepartamentoInfo {
        private Integer id;
        private String nome;
    }

    @Data
    public static class InstituicaoInfo {
        private Integer id;
        private String nome;
    }
} 