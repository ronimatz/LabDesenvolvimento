package com.projeto2.moedaEstudantil.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto2.moedaEstudantil.dto.CadastroProfessorDTO;
import com.projeto2.moedaEstudantil.dto.DepartamentoDTO;
import com.projeto2.moedaEstudantil.dto.InstituicaoDTO;
import com.projeto2.moedaEstudantil.dto.response.InstituicaoListDTO;
import com.projeto2.moedaEstudantil.dto.response.DepartamentoListDTO;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.model.enums.Role;
import com.projeto2.moedaEstudantil.repositories.DepartamentoRepository;
import com.projeto2.moedaEstudantil.repositories.InstituicaoEnsinoRepository;
import com.projeto2.moedaEstudantil.repositories.ProfessorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private InstituicaoEnsinoRepository instituicaoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public InstituicaoEnsino cadastrarInstituicao(InstituicaoDTO dto) {
        InstituicaoEnsino instituicao = new InstituicaoEnsino();
        instituicao.setNome(dto.getNome());
        instituicao.setCnpj(dto.getCnpj());
        return instituicaoRepository.save(instituicao);
    }

    public List<InstituicaoListDTO> listarInstituicoes() {
        List<InstituicaoEnsino> instituicoes = instituicaoRepository.findAll();
        return instituicoes.stream().map(this::toInstituicaoListDTO).collect(Collectors.toList());
    }

    private InstituicaoListDTO toInstituicaoListDTO(InstituicaoEnsino instituicao) {
        InstituicaoListDTO dto = new InstituicaoListDTO();
        dto.setId(instituicao.getId());
        dto.setNome(instituicao.getNome());
        dto.setCnpj(instituicao.getCnpj());
        
        if (instituicao.getDepartamentos() != null) {
            dto.setDepartamentos(instituicao.getDepartamentos().stream()
                .map(this::toDepartamentoListDTOForInstituicao)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private InstituicaoListDTO.DepartamentoListDTO toDepartamentoListDTOForInstituicao(Departamento departamento) {
        InstituicaoListDTO.DepartamentoListDTO dto = new InstituicaoListDTO.DepartamentoListDTO();
        dto.setId(departamento.getId());
        dto.setNome(departamento.getNome());
        
        if (departamento.getProfessores() != null) {
            dto.setProfessores(departamento.getProfessores().stream()
                .map(this::toProfessorListDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private InstituicaoListDTO.ProfessorListDTO toProfessorListDTO(Professor professor) {
        InstituicaoListDTO.ProfessorListDTO dto = new InstituicaoListDTO.ProfessorListDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setEmail(professor.getEmail());
        return dto;
    }

    @Transactional
    public Departamento cadastrarDepartamento(DepartamentoDTO dto) {
        InstituicaoEnsino instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        Departamento departamento = new Departamento();
        departamento.setNome(dto.getNome());
        departamento.setInstituicaoEnsino(instituicao);
        return departamentoRepository.save(departamento);
    }

    public List<Departamento> listarDepartamentosPorInstituicao(Integer instituicaoId) {
        return departamentoRepository.findByInstituicaoEnsinoId(instituicaoId);
    }

    @Transactional
    public Professor cadastrarProfessor(CadastroProfessorDTO dto) {
        InstituicaoEnsino instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        Departamento departamento = departamentoRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        if (!departamento.getInstituicaoEnsino().getId().equals(instituicao.getId())) {
            throw new RuntimeException("O departamento não pertence à instituição informada");
        }

        Professor professor = new Professor();
        professor.setNome(dto.getNome());
        professor.setEmail(dto.getEmail());
        professor.setCpf(dto.getCpf());
        professor.setSenha(passwordEncoder.encode(dto.getSenha()));
        professor.setDepartamento(departamento);
        professor.setInstituicaoEnsino(instituicao);
        professor.setSaldoMoedas(100); // Inicia com 100 moedas
        professor.setRole(Role.PROFESSOR); // Define o papel como PROFESSOR

        return professorRepository.save(professor);
    }

    public List<DepartamentoListDTO> listarDepartamentosPorInstituicaoDTO(Integer instituicaoId) {
        InstituicaoEnsino instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        return instituicao.getDepartamentos().stream()
                .map(this::toDepartamentoListDTO)
                .collect(Collectors.toList());
    }

    private DepartamentoListDTO toDepartamentoListDTO(Departamento departamento) {
        DepartamentoListDTO dto = new DepartamentoListDTO();
        dto.setId(departamento.getId());
        dto.setNome(departamento.getNome());
        return dto;
    }
} 