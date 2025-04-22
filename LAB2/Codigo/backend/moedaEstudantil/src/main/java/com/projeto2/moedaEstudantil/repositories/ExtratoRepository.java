package com.projeto2.moedaEstudantil.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto2.moedaEstudantil.model.Extrato;

@Repository
public interface ExtratoRepository extends JpaRepository<Extrato, Integer> {
    Optional<Extrato> findByUsuarioId(Integer usuarioId);
}