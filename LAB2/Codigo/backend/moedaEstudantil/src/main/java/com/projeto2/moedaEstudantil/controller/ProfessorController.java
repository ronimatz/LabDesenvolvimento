package com.projeto2.moedaEstudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.projeto2.moedaEstudantil.dto.EnvioMoedasDTO;
import com.projeto2.moedaEstudantil.dto.response.ProfessorInfoDTO;
import com.projeto2.moedaEstudantil.dto.response.TransacaoHistoricoDTO;
import com.projeto2.moedaEstudantil.services.ProfessorService;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;

    @GetMapping("/info")
    public ResponseEntity<ProfessorInfoDTO> getProfessorInfo(Authentication authentication) {
        ProfessorInfoDTO professor = professorService.getProfessorInfo(authentication);
        return ResponseEntity.ok(professor);
    }

    @PostMapping("/enviar-moedas")
    public ResponseEntity<Void> enviarMoedas(
            @Valid @RequestBody EnvioMoedasDTO envioMoedasDTO,
            Authentication authentication) {
        professorService.enviarMoedas(envioMoedasDTO, authentication);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/historico-envios")
    public ResponseEntity<List<TransacaoHistoricoDTO>> getHistoricoEnvios(Authentication authentication) {
        List<TransacaoHistoricoDTO> historico = professorService.getHistoricoEnvios(authentication);
        return ResponseEntity.ok(historico);
    }
} 