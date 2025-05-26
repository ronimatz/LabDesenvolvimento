package com.projeto2.moedaEstudantil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto2.moedaEstudantil.model.Aluno;

import java.util.Optional;

public interface AlunoRepository extends JpaRepository<Aluno,Integer> {
        boolean existsByCpf(String cpf);
        boolean existsByRg(String rg);
        Optional<Aluno> findByEmail(String email);
} 
