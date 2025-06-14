package com.projeto2.moedaEstudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto2.moedaEstudantil.dto.request.InstituicaoDTO;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.services.InstituicaoEnsinoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/instituicao-ensino")
public class InstituicaoEnsinoController {

    @Autowired
    private InstituicaoEnsinoService service;

    @PostMapping
    public ResponseEntity<InstituicaoEnsino> criarInstituicaoEnsino(@Valid @RequestBody InstituicaoDTO instituicaoDTO) {
        InstituicaoEnsino instituicaoEnsinoCriada = service.save(instituicaoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoEnsinoCriada);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InstituicaoEnsino> buscarInstituicaoEnsino(@PathVariable Integer id) {
        InstituicaoEnsino instituicaoEnsino = service.findById(id);
        return ResponseEntity.ok(instituicaoEnsino);
    }

    /* 
    @PostMapping("/add-professor/{instituicaoId}")
    public ResponseEntity<?> adicionarProfessor(@PathVariable Integer instituicaoId, @RequestBody Professor professor) {
        InstituicaoEnsino instituicao = service.findById(instituicaoId);

        // Verifica se o departamento existe e pertence à instituição
        Departamento departamento = professor.getDepartamento();
        if (departamento == null || departamento.getId() == null) {
            return ResponseEntity.badRequest().body("Departamento inválido");
        }

        Optional<Departamento> departamentoOptional = departamentoRepository.findById(departamento.getId());
        if (departamentoOptional.isEmpty()
                || !departamentoOptional.get().getInstituicaoEnsino().getId().equals(instituicaoId)) {
            return ResponseEntity.badRequest().body("Departamento não encontrado na instituição informada");
        }

        professor.setDepartamento(departamentoOptional.get());
        professor.setInstituicaoEnsino(instituicao);
        instituicao.adicionarProfessor(professor);

        professorRepository.save(professor);

        return ResponseEntity.ok(instituicao);
    }
        */
}
