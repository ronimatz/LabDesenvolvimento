package com.projeto2.moedaEstudantil.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projeto2.moedaEstudantil.model.TipoTransacao;
import com.projeto2.moedaEstudantil.model.Transacao;
import com.projeto2.moedaEstudantil.model.Usuario;

import java.util.List;

@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByOrigemOrderByDataDesc(Usuario origem);
    List<Transacao> findByDestinoOrderByDataDesc(Usuario destino);
    List<Transacao> findByTipoOrderByDataDesc(TipoTransacao tipo);
    List<Transacao> findByOrigemAndTipoOrderByDataDesc(Usuario origem, TipoTransacao tipo);
    List<Transacao> findByDestinoAndTipoOrderByDataDesc(Usuario destino, TipoTransacao tipo);
} 