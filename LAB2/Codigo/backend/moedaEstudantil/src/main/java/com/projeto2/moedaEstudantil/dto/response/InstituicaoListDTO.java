package com.projeto2.moedaEstudantil.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class InstituicaoListDTO {
    private Integer id;
    private String nome;
    private String cnpj;
    private List<DepartamentoListDTO> departamentos;

    @Data
    public static class DepartamentoListDTO {
        private Integer id;
        private String nome;
        private List<ProfessorListDTO> professores;
    }

    @Data
    public static class ProfessorListDTO {
        private Integer id;
        private String nome;
        private String email;
    }
} 