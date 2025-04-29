package com.projeto2.moedaEstudantil.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaParceiraDTO {
    private String email;
    private String senha;
    private String nome;
    private String cnpj;
    private List<VantagemDTO> vantagens; 
}