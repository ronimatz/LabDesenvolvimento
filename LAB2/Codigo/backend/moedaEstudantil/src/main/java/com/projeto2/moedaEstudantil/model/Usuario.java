package com.projeto2.moedaEstudantil.model;

import com.projeto2.moedaEstudantil.model.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;

import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
@NoArgsConstructor

public abstract class Usuario implements Autenticavel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuario_seq")
    @SequenceGenerator(name = "usuario_seq", sequenceName = "usuario_sequence", allocationSize = 1)
    protected Integer id;

    @Column(unique = true, nullable = false)
    protected String email;

    @Column(nullable = false)
    protected String senha;

    @Enumerated(EnumType.STRING)
    protected Role role;

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = senha;

    }

}
