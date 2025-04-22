package com.projeto2.moedaEstudantil.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.projeto2.moedaEstudantil.dto.AlunoDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoResponseDTO;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.Curso;
import com.projeto2.moedaEstudantil.model.Extrato;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.repositories.AlunoRepository;
import com.projeto2.moedaEstudantil.repositories.CursoRepository;

import com.projeto2.moedaEstudantil.repositories.InstituicaoEnsinoRepository;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InstituicaoEnsinoRepository instituicaoRepository;

    public AlunoResponseDTO criarAluno(AlunoDTO dto) {
        if (alunoRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
    
        if (alunoRepository.existsByRg(dto.getRg())) {
            throw new IllegalArgumentException("RG já cadastrado");
        }
    
        InstituicaoEnsino instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada"));
    
        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));
    
        if (!curso.getDepartamento().getInstituicaoEnsino().getId().equals(instituicao.getId())) {
            throw new IllegalArgumentException("Curso não pertence à instituição informada");
        }
    
        Aluno aluno = new Aluno(
                dto.getEmail(),
                dto.getSenha(),
                dto.getNome(),
                dto.getCpf(),
                dto.getRg(),
                dto.getEndereco(),
                instituicao,
                curso
        );
    
        
        Extrato extrato = new Extrato();
        extrato.setUsuario(aluno);
        extrato.setSaldoMoedas(0);
        aluno.setExtrato(extrato);

        aluno = alunoRepository.save(aluno);
    
        return mapToResponseDTO(aluno);
    }

    public List<AlunoResponseDTO> listarTodos() {
        return alunoRepository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public AlunoResponseDTO buscarPorId(Integer id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
        return mapToResponseDTO(aluno);
    }

    public void deletarAluno(Integer id) {
        if (!alunoRepository.existsById(id)) {
            throw new IllegalArgumentException("Aluno não encontrado");
        }
        alunoRepository.deleteById(id);
    }

    private AlunoResponseDTO mapToResponseDTO(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId().intValue(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getEmail(),
                aluno.getRg(),
                aluno.getEndereco(),
                aluno.getCurso().getNome(),
                aluno.getInstituicaoEnsino().getNome());
    }
}