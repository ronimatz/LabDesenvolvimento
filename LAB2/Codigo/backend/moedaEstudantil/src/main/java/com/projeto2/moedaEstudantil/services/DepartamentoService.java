package com.projeto2.moedaEstudantil.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto2.moedaEstudantil.dto.request.DepartamentoDTO;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.repositories.DepartamentoRepository;
import com.projeto2.moedaEstudantil.repositories.InstituicaoEnsinoRepository;

@Service
public class DepartamentoService {
    
    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private InstituicaoEnsinoRepository instituicaoEnsinoRepository;

    public Departamento save(DepartamentoDTO dto) {
        InstituicaoEnsino instituicao = instituicaoEnsinoRepository.findById(dto.getInstituicaoId())
            .orElseThrow(() -> new IllegalArgumentException("Instituição de Ensino não cadastrada"));

        // Verifica se já existe um departamento com o mesmo nome na instituição
        boolean nomeRepetido = instituicao.getDepartamentos().stream()
            .anyMatch(dep -> dep.getNome().equalsIgnoreCase(dto.getNome()));
        if (nomeRepetido) {
            throw new IllegalArgumentException("Departamento com o nome " + dto.getNome() + " já existe na instituição.");
        }

        Departamento departamento = new Departamento();
        departamento.setNome(dto.getNome());
        departamento.setInstituicaoEnsino(instituicao);

        return departamentoRepository.save(departamento);
    }

    // Manter método antigo para compatibilidade
    public Departamento save(Departamento departamento) {
        departamento.setId(null);

        Integer idInstituicao = departamento.getInstituicaoEnsino().getId();
        InstituicaoEnsino instituicao = instituicaoEnsinoRepository.findById(idInstituicao)
            .orElseThrow(() -> new IllegalArgumentException("Instituição de Ensino não cadastrada"));

        
        boolean nomeRepetido = instituicao.getDepartamentos().stream()
            .anyMatch(dep -> dep.getNome().equalsIgnoreCase(departamento.getNome()));
        if (nomeRepetido) {
            throw new IllegalArgumentException("Departamento com o nome " + departamento.getNome() + " já existe na instituição.");
        }

        
        departamento.setInstituicaoEnsino(instituicao);
        instituicao.getDepartamentos().add(departamento); 

        return departamentoRepository.save(departamento);
    }
}
