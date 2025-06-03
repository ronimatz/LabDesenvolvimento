package com.projeto2.moedaEstudantil.dto.request;

import java.util.List;

import com.projeto2.moedaEstudantil.dto.response.VantagemDTO;
import com.projeto2.moedaEstudantil.validation.CnpjValido;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaParceiraDTO {
    private Integer id;
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;
    @NotBlank(message = "Senha é obrigatória")
    private String senha;
    @NotBlank(message = "Nome é obrigatório")
    private String nome;
    @NotBlank(message = "CNPJ é obrigatório")
    @CnpjValido
    private String cnpj;
    private List<VantagemDTO> vantagens; 
}