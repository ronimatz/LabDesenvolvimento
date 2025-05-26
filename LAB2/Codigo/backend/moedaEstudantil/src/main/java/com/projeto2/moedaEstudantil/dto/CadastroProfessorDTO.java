package com.projeto2.moedaEstudantil.dto;


import com.validation.CpfValido;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CadastroProfessorDTO {
    
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "O CPF é obrigatório")
    @CpfValido(message = "CPF inválido")
    private String cpf;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotNull(message = "O ID do departamento é obrigatório")
    private Integer departamentoId;

    @NotNull(message = "O ID da instituição é obrigatório")
    private Integer instituicaoId;
} 