package com.projeto2.moedaEstudantil.dto;

import com.projeto2.moedaEstudantil.model.Endereco;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {
    private String email;
    private String senha;
    private String nome;
    private String cpf;
    private String rg;
    private Endereco endereco;
    private Integer instituicaoId;
    private Integer cursoId;
}