package com.projeto2.moedaEstudantil.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto2.moedaEstudantil.model.EmpresaParceira;

@Repository
public interface EmpresaParceiraRepository extends JpaRepository<EmpresaParceira, Integer> {

    Optional<EmpresaParceira> findByNome(String nome);

    boolean existsByEmail(String email);

    boolean existsByCnpj(String cnpj);

} 