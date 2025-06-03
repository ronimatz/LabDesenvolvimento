package com.projeto2.moedaEstudantil.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto2.moedaEstudantil.dto.request.InstituicaoDTO;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.repositories.InstituicaoEnsinoRepository;

@Service
public class InstituicaoEnsinoService {
    
    @Autowired
    private InstituicaoEnsinoRepository repository;

    public InstituicaoEnsino save(InstituicaoDTO dto) {
        if (repository.findByNome(dto.getNome()).isPresent()) {
            throw new RuntimeException("Instituição Ensino já cadastrada");
        }

        if (repository.existsByCnpj(dto.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado");
        }

        InstituicaoEnsino instituicaoEnsino = new InstituicaoEnsino();
        instituicaoEnsino.setNome(dto.getNome());
        instituicaoEnsino.setCnpj(dto.getCnpj());

        return repository.save(instituicaoEnsino);
    }

    public InstituicaoEnsino save(InstituicaoEnsino instituicaoEnsino) {
        instituicaoEnsino.setId(null);
        
        if (repository.findByNome(instituicaoEnsino.getNome()).isPresent()) {
            throw new RuntimeException("Instituicao Ensino ja cadastrada");
        }

        return repository.save(instituicaoEnsino);
    }

    public void addProfessor(Integer instuicaoId, Professor professor) {
        InstituicaoEnsino instituicao = repository.findById(instuicaoId)
            .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        
        instituicao.adicionarProfessor(professor);

        repository.save(instituicao); 
    }

    public void addDepartamento(Integer instuicaoId, Departamento departamento) {
        InstituicaoEnsino instituicao = repository.findById(instuicaoId)
            .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        
        instituicao.adicionarDepartamento(departamento);

        repository.save(instituicao); 
    }

    public InstituicaoEnsino findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Instituição Ensino nao encontrada"));
    }
}
