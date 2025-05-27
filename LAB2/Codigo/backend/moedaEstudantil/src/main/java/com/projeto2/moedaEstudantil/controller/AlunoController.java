package com.projeto2.moedaEstudantil.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto2.moedaEstudantil.dto.AlunoDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoResponseDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoInfoDTO;
import com.projeto2.moedaEstudantil.dto.response.TransacaoRecebimentoDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemAdquiridaDTO;
import com.projeto2.moedaEstudantil.services.AlunoService;

@RestController
@RequestMapping("/alunos")
public class AlunoController {

    @Autowired
    private AlunoService alunoService;

    @PostMapping
    public ResponseEntity<AlunoResponseDTO> criar(@RequestBody AlunoDTO dto) {
        AlunoResponseDTO alunoCriado = alunoService.criarAluno(dto);
        return ResponseEntity.ok(alunoCriado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> editarAluno(@PathVariable Integer id, @RequestBody AlunoDTO dto) {
        return ResponseEntity.ok(alunoService.editarAluno(id, dto));
    }

    @GetMapping
    public ResponseEntity<List<AlunoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(alunoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlunoResponseDTO> buscarPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(alunoService.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        alunoService.deletarAluno(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/info")
    public ResponseEntity<AlunoInfoDTO> getInfo(Authentication authentication) {
        return ResponseEntity.ok(alunoService.getInfo(authentication));
    }

    @GetMapping("/historico-recebimentos")
    public ResponseEntity<List<TransacaoRecebimentoDTO>> getHistoricoRecebimentos(Authentication authentication) {
        return ResponseEntity.ok(alunoService.getHistoricoRecebimentos(authentication));
    }

    @GetMapping("/vantagens-adquiridas")
    public ResponseEntity<List<VantagemAdquiridaDTO>> getVantagensAdquiridas(Authentication authentication) {
        return ResponseEntity.ok(alunoService.getVantagensAdquiridas(authentication));
    }
}