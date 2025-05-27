package com.projeto2.moedaEstudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.projeto2.moedaEstudantil.dto.request.CadastroCursoDTO;
import com.projeto2.moedaEstudantil.dto.request.CadastroProfessorDTO;
import com.projeto2.moedaEstudantil.dto.request.DepartamentoDTO;
import com.projeto2.moedaEstudantil.dto.request.InstituicaoDTO;
import com.projeto2.moedaEstudantil.dto.response.InstituicaoListDTO;
import com.projeto2.moedaEstudantil.dto.response.DepartamentoListDTO;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.services.AdminService;
import com.projeto2.moedaEstudantil.services.CursoService;
import com.projeto2.moedaEstudantil.dto.response.CursoListDTO;
import com.projeto2.moedaEstudantil.dto.response.CursoResponseDTO;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CursoService cursoService;

    @PostMapping("/instituicoes")
    public ResponseEntity<InstituicaoEnsino> cadastrarInstituicao(@Valid @RequestBody InstituicaoDTO instituicaoDTO) {
        InstituicaoEnsino instituicao = adminService.cadastrarInstituicao(instituicaoDTO);
        return ResponseEntity.ok(instituicao);
    }

    @GetMapping("/instituicoes")
    public ResponseEntity<List<InstituicaoListDTO>> listarInstituicoes() {
        List<InstituicaoListDTO> instituicoes = adminService.listarInstituicoes();
        return ResponseEntity.ok(instituicoes);
    }

    @PostMapping("/departamentos")
    public ResponseEntity<Departamento> cadastrarDepartamento(@Valid @RequestBody DepartamentoDTO departamentoDTO) {
        Departamento departamento = adminService.cadastrarDepartamento(departamentoDTO);
        return ResponseEntity.ok(departamento);
    }

    @GetMapping("/instituicoes/{instituicaoId}/departamentos")
    public ResponseEntity<List<DepartamentoListDTO>> listarDepartamentosPorInstituicao(@PathVariable Integer instituicaoId) {
        List<DepartamentoListDTO> departamentos = adminService.listarDepartamentosPorInstituicaoDTO(instituicaoId);
        return ResponseEntity.ok(departamentos);
    }

    @PostMapping("/professores")
    public ResponseEntity<Professor> cadastrarProfessor(@Valid @RequestBody CadastroProfessorDTO professorDTO) {
        Professor professor = adminService.cadastrarProfessor(professorDTO);
        return ResponseEntity.ok(professor);
    }

    @PostMapping("/cursos")
    public ResponseEntity<CursoResponseDTO> cadastrarCurso(@Valid @RequestBody CadastroCursoDTO dto) {
        CursoResponseDTO curso = cursoService.cadastrarCurso(dto);
        return ResponseEntity.ok(curso);
    }

    @GetMapping("/instituicoes/{instituicaoId}/cursos")
    public ResponseEntity<List<CursoListDTO>> listarCursosPorInstituicao(@PathVariable Integer instituicaoId) {
        List<CursoListDTO> cursos = cursoService.listarCursosPorInstituicao(instituicaoId);
        return ResponseEntity.ok(cursos);
    }

    @PostMapping("/distribuir-moedas")
    public ResponseEntity<String> distribuirMoedasParaProfessores() {
        adminService.distribuirMoedasParaProfessores();
        return ResponseEntity.ok("Moedas distribuídas com sucesso para todos os professores");
    }
} 