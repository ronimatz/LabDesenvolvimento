package com.projeto2.moedaEstudantil.model;

import java.util.ArrayList;
import java.util.List;


import com.projeto2.moedaEstudantil.model.enums.Role;
import com.validation.CnpjValido;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "empresas_parceiras")
@Data
@EqualsAndHashCode(callSuper = true)
public class EmpresaParceira extends Usuario {
    
    @NotBlank(message = "CNPJ é obrigatório")
    @CnpjValido
    @Column(unique = true)
    private String cnpj;

    @NotBlank(message = "Nome fantasia é obrigatório")
    private String nome;

    private String descricao;

    private String endereco;

    private String telefone;

    @OneToMany(mappedBy = "empresaParceiraResponsavel", cascade = CascadeType.ALL)
    private List<Vantagem> vantagens = new ArrayList<>();

    public EmpresaParceira(String email, String senha, String nome, String cnpj) {
        super(email, senha);
        this.role = Role.EMPRESA_PARCEIRA;
        this.nome = nome;
        setCnpj(cnpj);
    }

    public EmpresaParceira() {
        super();
    }

    public void setCnpj(String cnpj) {
        if (cnpj != null) {
            // Remove caracteres não numéricos
            String numeros = cnpj.replaceAll("[^0-9]", "");
            
            // Formata o CNPJ se tiver 14 dígitos
            if (numeros.length() == 14) {
                this.cnpj = numeros.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5");
            } else {
                this.cnpj = cnpj;
            }
        }
    }
}
