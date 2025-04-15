package com.projeto2.moedaEstudantil.model.enums;

public enum Role {
    ADMIN(0, "ROLE_ADMIN"),
    ALUNO(1, "ROLE_ALUNO"),
    PROFESSOR(2, "ROLE_PROFESSOR"),
    EMPRESA_PARCEIRA(3, "ROLE_EMPRESA_PARCEIRA");

    private Integer cod;
    private String descricao;

    private Role(Integer cod, String descricao) {
        this.cod = cod;
        this.descricao = descricao;
    }

    public Integer getCod() {
        return cod;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Role toEnum(Integer cod) {
        if (cod == null) {
            return null;
        }
        for (Role x : Role.values()) {
            if (cod.equals(x.getCod())) {
                return x;
            }
        }
        throw new IllegalArgumentException("Id inválido: " + cod);
    }
}
