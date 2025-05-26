package com.projeto2.moedaEstudantil.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.projeto2.moedaEstudantil.model.Departamento;

import java.util.List;

public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {;
    List<Departamento> findByInstituicaoEnsinoId(Integer instituicaoId);
    Optional<Departamento> findByNome(String nome);
} 
