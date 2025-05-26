package com.projeto2.moedaEstudantil.model;

import com.projeto2.moedaEstudantil.model.enums.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "admins")
@Getter
@Setter
@NoArgsConstructor
public class Admin extends Usuario {
    
    private String nome;
    
    public Admin(String email, String senha) {
        super(email, senha);
        this.setRole(Role.ADMIN);
    }
}
