package com.projeto2.moedaEstudantil.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Vantagem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "A descrição é obrigatória")
    @Column(nullable = false)
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    @Min(value = 0, message = "O valor não pode ser negativo")
    @Column(nullable = false)
    private Double valor;

    @Lob
    @Column(name = "foto_produto", columnDefinition = "BLOB")
    private byte[] fotoProduto;

    @NotNull(message = "O desconto é obrigatório")
    @Min(value = 0, message = "O desconto não pode ser negativo")
    @Max(value = 100, message = "O desconto não pode ser maior que 100%")
    @Column(nullable = false)
    private Double desconto;

    @NotNull(message = "A empresa parceira responsável é obrigatória")
    @ManyToOne
    @JoinColumn(name = "empresa_parceira_id") 
    @JsonIgnore
    private EmpresaParceira empresaParceiraResponsavel;

    public Vantagem() {
    }

    public Vantagem(Integer id, String descricao, Double valor, byte[] fotoProduto, Double desconto, EmpresaParceira empresaParceiraResponsavel) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.fotoProduto = fotoProduto;
        this.desconto = desconto;
        this.empresaParceiraResponsavel = empresaParceiraResponsavel;
    }
}
