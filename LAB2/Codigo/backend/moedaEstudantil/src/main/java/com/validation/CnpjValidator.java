package com.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CnpjValidator implements ConstraintValidator<CnpjValido, String> {

    @Override
    public void initialize(CnpjValido constraintAnnotation) {
    }

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null) {
            return false;
        }

        // Remove caracteres não numéricos
        cnpj = cnpj.replaceAll("[^0-9]", "");

        // Verifica se tem 14 dígitos
        if (cnpj.length() != 14) {
            return false;
        }

        // Verifica se todos os dígitos são iguais
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }

        // Calcula primeiro dígito verificador
        int soma = 0;
        int peso = 2;
        for (int i = 11; i >= 0; i--) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int digito1 = 11 - (soma % 11);
        digito1 = digito1 > 9 ? 0 : digito1;

        // Verifica primeiro dígito
        if (Character.getNumericValue(cnpj.charAt(12)) != digito1) {
            return false;
        }

        // Calcula segundo dígito verificador
        soma = 0;
        peso = 2;
        for (int i = 12; i >= 0; i--) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = peso == 9 ? 2 : peso + 1;
        }
        int digito2 = 11 - (soma % 11);
        digito2 = digito2 > 9 ? 0 : digito2;

        // Verifica segundo dígito
        return Character.getNumericValue(cnpj.charAt(13)) == digito2;
    }
} 