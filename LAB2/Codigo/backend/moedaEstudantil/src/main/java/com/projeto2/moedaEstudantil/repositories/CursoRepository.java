package com.projeto2.moedaEstudantil.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto2.moedaEstudantil.model.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Integer> {

    Optional<Curso> findByNome(String nome);
    
    List<Curso> findByDepartamentoInstituicaoEnsinoId(Integer instituicaoId);
} 
