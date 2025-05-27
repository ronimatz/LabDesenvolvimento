package com.projeto2.moedaEstudantil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto2.moedaEstudantil.dto.request.ResgatarVantagemDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoInfoDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoResponseDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemResgateDTO;
import com.projeto2.moedaEstudantil.dto.response.TransacaoRecebimentoDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemResgatadaDTO;
import com.projeto2.moedaEstudantil.services.AlunoService;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @GetMapping
    @PreAuthorize("hasAnyRole('PROFESSOR', 'ADMIN')")
    public ResponseEntity<List<AlunoResponseDTO>> listarAlunos() {
        List<AlunoResponseDTO> alunos = alunoService.listarAlunos();
        return ResponseEntity.ok(alunos);
    }

    @GetMapping("/info")
    public ResponseEntity<AlunoInfoDTO> getAlunoInfo(Authentication authentication) {
        AlunoInfoDTO aluno = alunoService.getAlunoInfo(authentication);
        return ResponseEntity.ok(aluno);
    }

    @PostMapping("/resgatar-vantagem")
    public ResponseEntity<VantagemResgateDTO> resgatarVantagem(@RequestBody ResgatarVantagemDTO dto, Authentication authentication) {
        VantagemResgateDTO resultado = alunoService.resgatarVantagem(dto.getVantagemId(), authentication);
        return ResponseEntity.ok(resultado);
    }

    @GetMapping("/vantagens-resgatadas")
    public ResponseEntity<List<VantagemResgatadaDTO>> listarVantagensResgatadas(Authentication authentication) {
        List<VantagemResgatadaDTO> vantagens = alunoService.listarVantagensResgatadas(authentication);
        return ResponseEntity.ok(vantagens);
    }

    @GetMapping("/historico-recebimentos")
    public ResponseEntity<List<TransacaoRecebimentoDTO>> listarRecebimentos(Authentication authentication) {
        List<TransacaoRecebimentoDTO> recebimentos = alunoService.listarRecebimentos(authentication);
        return ResponseEntity.ok(recebimentos);
    }
}