package com.projeto2.moedaEstudantil.dto.request;

import com.projeto2.moedaEstudantil.validation.CnpjValido;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EmpresaParceiraRequestDTO {
    
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    private String senha;

    @NotBlank(message = "CNPJ é obrigatório")
    @CnpjValido
    private String cnpj;

    @NotBlank(message = "Nome fantasia é obrigatório")
    private String nomeFantasia;

    private String descricao;

    private String endereco;

    private String telefone;
} 