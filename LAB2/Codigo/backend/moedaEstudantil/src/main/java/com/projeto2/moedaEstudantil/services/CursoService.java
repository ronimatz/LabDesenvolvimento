package com.projeto2.moedaEstudantil.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto2.moedaEstudantil.dto.request.CadastroCursoDTO;
import com.projeto2.moedaEstudantil.dto.response.CursoResponseDTO;
import com.projeto2.moedaEstudantil.dto.response.CursoListDTO;
import com.projeto2.moedaEstudantil.model.Curso;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.repositories.CursoRepository;
import com.projeto2.moedaEstudantil.repositories.DepartamentoRepository;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Transactional
    public CursoResponseDTO cadastrarCurso(CadastroCursoDTO dto) {
        Departamento departamento = departamentoRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        if (cursoRepository.findByNome(dto.getNome()).isPresent()) {
            throw new RuntimeException("Curso com esse nome já existe");
        }

        Curso curso = new Curso();
        curso.setNome(dto.getNome());
        curso.setCargaHoraria(dto.getCargaHoraria());
        curso.setDepartamento(departamento);

        curso = cursoRepository.save(curso);

        return toDTO(curso);
    }

    public List<CursoResponseDTO> getAllCursos() {
        return cursoRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CursoResponseDTO getCursoById(Integer id) {
        Curso curso = cursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));
        return toDTO(curso);
    }

    @Transactional
    public CursoResponseDTO updateCurso(Integer id, CadastroCursoDTO dto) {
        Curso curso = cursoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Departamento departamento = departamentoRepository.findById(dto.getDepartamentoId())
            .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        curso.setNome(dto.getNome());
        curso.setCargaHoraria(dto.getCargaHoraria());
        curso.setDepartamento(departamento);

        return toDTO(cursoRepository.save(curso));
    }

    public void deleteCurso(Integer id) {
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso não encontrado");
        }
        cursoRepository.deleteById(id);
    }

    public List<CursoListDTO> listarCursosPorInstituicao(Integer instituicaoId) {
        List<Curso> cursos = cursoRepository.findByDepartamentoInstituicaoEnsinoId(instituicaoId);
        return cursos.stream()
                .map(this::toCursoListDTO)
                .collect(Collectors.toList());
    }

    private CursoListDTO toCursoListDTO(Curso curso) {
        CursoListDTO dto = new CursoListDTO();
        dto.setId(curso.getId());
        dto.setNome(curso.getNome());
        dto.setDepartamentoNome(curso.getDepartamento().getNome());
        return dto;
    }

    private CursoResponseDTO toDTO(Curso curso) {
        return new CursoResponseDTO(
                curso.getId(),
                curso.getNome(),
                curso.getCargaHoraria(),
                curso.getDepartamento().getNome()
        );
    }
}