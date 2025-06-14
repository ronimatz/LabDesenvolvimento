package com.projeto2.moedaEstudantil.dto.request;


import com.projeto2.moedaEstudantil.validation.CpfValido;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlunoDTO {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @NotBlank(message = "O CPF é obrigatório")
    @CpfValido
    private String cpf;

    @NotBlank(message = "O RG é obrigatório")
    private String rg;

    @NotBlank(message = "A rua é obrigatória")
    private String rua;

    @NotNull(message = "O número é obrigatório")
    private Integer numero;

    private String complemento;

    @NotBlank(message = "O bairro é obrigatório")
    private String bairro;

    @NotBlank(message = "A cidade é obrigatória")
    private String cidade;

    @NotBlank(message = "O estado é obrigatório")
    private String estado;

    @NotNull(message = "O ID da instituição é obrigatório")
    @Min(value = 1, message = "O ID da instituição deve ser maior que 0")
    private Integer instituicaoId;

    @NotNull(message = "O ID do curso é obrigatório")
    @Min(value = 1, message = "O ID do curso deve ser maior que 0")
    private Integer cursoId;
}