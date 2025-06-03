package com.projeto2.moedaEstudantil.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transacoes")
@Data
@NoArgsConstructor
public class Transacao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Usuario origem;

    @ManyToOne
    private Usuario destino;

    private LocalDateTime data;
    
    private Double valor;
    
    private String motivo;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    // Campo opcional para cupom (usado em transações de vantagens)
    private String cupomGerado;

    // Campo opcional para referência à vantagem (usado em transações de vantagens)
    @ManyToOne
    private Vantagem vantagem;

    public Transacao(Usuario origem, Usuario destino, Double valor, String motivo, TipoTransacao tipo) {
        this.origem = origem;
        this.destino = destino;
        this.valor = valor;
        this.motivo = motivo;
        this.tipo = tipo;
        this.data = LocalDateTime.now();
    }

    // Construtor específico para transações de vantagens
    public static Transacao criarTransacaoVantagem(Aluno aluno, EmpresaParceira empresa, Vantagem vantagem, Double valor) {
        Transacao transacao = new Transacao(aluno, empresa, valor, "Resgate de vantagem: " + vantagem.getDescricao(), TipoTransacao.RESGATE_VANTAGEM);
        transacao.setVantagem(vantagem);
        transacao.setCupomGerado(gerarCupom());
        return transacao;
    }

    private static String gerarCupom() {
        return "CUPOM-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }
} 