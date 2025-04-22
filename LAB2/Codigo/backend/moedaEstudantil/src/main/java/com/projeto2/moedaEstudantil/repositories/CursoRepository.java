package com.projeto2.moedaEstudantil.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projeto2.moedaEstudantil.model.Curso;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

    Optional<Curso> findByNome(String nome);
    
} 
