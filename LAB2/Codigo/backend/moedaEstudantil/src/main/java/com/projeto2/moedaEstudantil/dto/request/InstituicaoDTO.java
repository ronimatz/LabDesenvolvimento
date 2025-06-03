package com.projeto2.moedaEstudantil.dto.request;

import com.projeto2.moedaEstudantil.validation.CnpjValido;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InstituicaoDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    @CnpjValido(message = "CNPJ inválido")
    private String cnpj;
} 