package com.projeto2.moedaEstudantil.dto.response;

import com.projeto2.moedaEstudantil.model.Endereco;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AlunoResponseDTO {
    private Integer id;
    private String nome;
    private String cpf;
    private String email;
    private String rg;
    private Endereco endereco;
    private String curso;
    private String instituicao;
}