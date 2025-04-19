package com.projeto2.moedaEstudantil.model;

import com.projeto2.moedaEstudantil.model.enums.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Admin extends Usuario{
    
    private Role role;

    public Admin(String email, String senha) {
        super(email, senha);
        this.role = Role.ADMIN;
    }
}
