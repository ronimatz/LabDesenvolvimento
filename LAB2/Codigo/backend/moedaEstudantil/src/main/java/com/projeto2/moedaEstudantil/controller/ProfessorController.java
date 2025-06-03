package com.projeto2.moedaEstudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.projeto2.moedaEstudantil.dto.request.EnvioMoedasDTO;
import com.projeto2.moedaEstudantil.dto.response.ProfessorInfoDTO;
import com.projeto2.moedaEstudantil.dto.response.TransacaoHistoricoDTO;
import com.projeto2.moedaEstudantil.services.ProfessorService;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public ResponseEntity<?> enviarMoedas(
            @Valid @RequestBody EnvioMoedasDTO envioMoedasDTO,
            BindingResult result,
            Authentication authentication) {
        
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> 
                errors.put(error.getField(), error.getDefaultMessage())
            );
            return ResponseEntity.badRequest().body(errors);
        }

        try {
            professorService.enviarMoedas(envioMoedasDTO, authentication);
            return ResponseEntity.ok().body(Map.of("message", "Moedas enviadas com sucesso"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/historico-envios")
    public ResponseEntity<List<TransacaoHistoricoDTO>> getHistoricoEnvios(Authentication authentication) {
        List<TransacaoHistoricoDTO> historico = professorService.getHistoricoEnvios(authentication);
        return ResponseEntity.ok(historico);
    }
} 