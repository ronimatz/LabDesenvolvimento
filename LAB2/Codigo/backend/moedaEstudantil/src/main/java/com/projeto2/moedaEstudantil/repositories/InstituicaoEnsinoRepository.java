package com.projeto2.moedaEstudantil.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;

public interface InstituicaoEnsinoRepository extends JpaRepository<InstituicaoEnsino, Integer>{
    Optional<InstituicaoEnsino> findByNome(String nome);
}
