package com.validation;



import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidCPF implements ConstraintValidator<CpfValido, String>{

	@Override
	public boolean isValid(String cpf, ConstraintValidatorContext context) {

		if (cpf == null || !cpf.matches("[0-9]{11}")) {
            return false;
        }

        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.digit(cpf.charAt(i), 10) * (10 - i);
        }
        
        int resto = soma % 11;
        int primeiroDigitoVerificador = resto == 0 || resto == 1 ? 0 : 11 - resto;

        if (primeiroDigitoVerificador != Character.digit(cpf.charAt(9), 10)) {
            return false;
        }

        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.digit(cpf.charAt(i), 10) * (11 - i);
        }
        
        resto = soma % 11;
        int segundoDigitoVerificador = resto == 0 || resto == 1 ? 0 : 11 - resto;

        return segundoDigitoVerificador == Character.digit(cpf.charAt(10), 10);
    }

	}


