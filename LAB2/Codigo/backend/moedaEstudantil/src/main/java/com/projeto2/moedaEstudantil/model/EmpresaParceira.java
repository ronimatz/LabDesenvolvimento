package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.validator.constraints.br.CNPJ;

import com.projeto2.moedaEstudantil.model.enums.Role;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "empresas_parceiras")
@Getter
@Setter
@NoArgsConstructor
public class EmpresaParceira extends Usuario {
    
    @Column(unique = true, nullable = false)
    private String nome;

    @OneToMany(mappedBy = "empresaParceiraResponsavel", cascade = CascadeType.ALL)
    private List<Vantagem> vantagens = new ArrayList<>();

    @Column(unique = true, nullable = false)
    @CNPJ(message = "CNPJ inválido")
    private String cnpj;

    public EmpresaParceira(String email, String senha, String nome, String cnpj) {
        super(email, senha);
        this.role = Role.EMPRESA_PARCEIRA;
        this.nome = nome;
        this.cnpj = cnpj;
        
    }
}
