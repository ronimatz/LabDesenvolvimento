package com.projeto2.moedaEstudantil.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class Transacao {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "professor_id")
    private Professor professor;

    @ManyToOne
    @JoinColumn(name = "aluno_id")
    private Aluno aluno;

    private Integer quantidade;
    private String motivo;
    private LocalDateTime data;

    @PrePersist
    protected void onCreate() {
        data = LocalDateTime.now();
    }
} 