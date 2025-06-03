package com.projeto2.moedaEstudantil.controller;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto2.moedaEstudantil.dto.response.VantagemDTO;
import com.projeto2.moedaEstudantil.dto.request.VantagemRequestDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemResgatadaDTO;
import com.projeto2.moedaEstudantil.model.EmpresaParceira;
import com.projeto2.moedaEstudantil.model.Vantagem;
import com.projeto2.moedaEstudantil.services.EmpresaParceiraService;
import com.projeto2.moedaEstudantil.dto.request.EmpresaParceiraDTO;

import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/empresas-parceiras")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraService empresaParceiraService;


    @GetMapping
    public List<EmpresaParceiraDTO> listarEmpresas() {
        List<EmpresaParceira> empresas = empresaParceiraService.listarTodas();
        return empresas.stream()
                .map(empresa -> new EmpresaParceiraDTO(
                        empresa.getId(),
                        empresa.getEmail(), empresa.getSenha(), empresa.getNome(),
                        empresa.getCnpj(), empresa.getVantagens().stream()
                                .map(v -> new VantagemDTO(v.getId(), v.getDescricao(), v.getValor(), v.getDesconto(),
                                        v.getFotoProduto() != null ? Base64.getEncoder().encodeToString(v.getFotoProduto()) : null))
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmpresaPorId(@PathVariable Integer id) {
        try {
            EmpresaParceira empresa = empresaParceiraService.buscarPorId(id);
            List<VantagemDTO> vantagensDTO = empresa.getVantagens().stream()
                    .map(v -> new VantagemDTO(v.getId(), v.getDescricao(), v.getValor(), v.getDesconto(),
                            v.getFotoProduto() != null ? Base64.getEncoder().encodeToString(v.getFotoProduto()) : null))
                    .collect(Collectors.toList());
            EmpresaParceiraDTO dto = new EmpresaParceiraDTO(
                    empresa.getId(), empresa.getEmail(), empresa.getSenha(), empresa.getNome(), empresa.getCnpj(), vantagensDTO
            );
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editarEmpresa(@PathVariable Integer id, @RequestBody EmpresaParceiraDTO dto) {
        try {
            EmpresaParceira empresaAtualizada = empresaParceiraService.editarEmpresa(id, dto);
            return ResponseEntity.ok(empresaAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{empresaId}/vantagens")
    public ResponseEntity<?> adicionarVantagem(@PathVariable Integer empresaId, @Valid @RequestBody VantagemRequestDTO vantagemDTO) {
        try {
            Vantagem novaVantagem = empresaParceiraService.adicionarVantagem(empresaId, vantagemDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(novaVantagem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/vantagens/{vantagemId}")
    public ResponseEntity<?> editarVantagem(@PathVariable Integer vantagemId, @Valid @RequestBody VantagemRequestDTO vantagemDTO) {
        try {
            Vantagem vantagemAtualizada = empresaParceiraService.editarVantagem(vantagemId, vantagemDTO);
            return ResponseEntity.ok(vantagemAtualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{empresaId}/vantagens/{vantagemId}")
    public ResponseEntity<?> removerVantagem(@PathVariable Integer empresaId, @PathVariable Integer vantagemId) {
        try {
            empresaParceiraService.removerVantagem(empresaId, vantagemId);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEmpresa(@PathVariable Integer id) {
        try {
            empresaParceiraService.deletarEmpresa(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa com ID '" + id + "' não encontrada.");
        }
    }

    @PostMapping("/cadastro")
    public ResponseEntity<Void> cadastrarEmpresaParceira(@Valid @RequestBody EmpresaParceiraDTO dto) {
        empresaParceiraService.cadastrarEmpresaParceira(dto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/info")
    public ResponseEntity<?> getEmpresaInfo(Authentication authentication) {
        try {
            String email = authentication.getName();
            EmpresaParceira empresa = empresaParceiraService.buscarPorEmail(email);
            EmpresaParceiraDTO dto = new EmpresaParceiraDTO(
                empresa.getId(),
                empresa.getEmail(),
                null, // Don't send password
                empresa.getNome(),
                empresa.getCnpj(),
                null // Don't send vantagens in this endpoint
            );
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{empresaId}/vantagens-compradas")
    public ResponseEntity<?> listarVantagensCompradas(@PathVariable Integer empresaId) {
        try {
            List<VantagemResgatadaDTO> vantagens = empresaParceiraService.listarVantagensCompradas(empresaId);
            return ResponseEntity.ok(vantagens);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}