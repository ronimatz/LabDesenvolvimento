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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
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

    @NotNull(message = "O usuário de origem é obrigatório")
    @ManyToOne
    private Usuario origem;

    @NotNull(message = "O usuário de destino é obrigatório")
    @ManyToOne
    private Usuario destino;

    @NotNull(message = "A data é obrigatória")
    private LocalDateTime data;
    
    @NotNull(message = "O valor é obrigatório")
    @Min(value = 0, message = "O valor não pode ser negativo")
    private Double valor;
    
    @NotBlank(message = "O motivo é obrigatório")
    private String motivo;

    @NotNull(message = "O tipo de transação é obrigatório")
    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    
    private String cupomGerado;

    
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