package com.projeto2.moedaEstudantil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.model.Transacao;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByProfessorOrderByDataDesc(Professor professor);
} 