package com.projeto2.moedaEstudantil.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projeto2.moedaEstudantil.model.EmpresaParceira;
import org.springframework.beans.factory.annotation.Autowired;
import com.projeto2.moedaEstudantil.repositories.EmpresaParceiraRepository;

@RestController
@RequestMapping("/empresas-parceiras")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraRepository empresaParceiraRepository;

    @GetMapping
    public List<EmpresaParceira> listarTodas() {
        return empresaParceiraRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaParceira> buscarPorId(@PathVariable Integer id) {
        Optional<EmpresaParceira> empresa = empresaParceiraRepository.findById(id);
        return empresa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<EmpresaParceira> criarEmpresa(@RequestBody EmpresaParceira empresa) {
        if (empresa.getNome() == null || empresa.getEmail() == null || empresa.getCnpj() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        EmpresaParceira novaEmpresa = empresaParceiraRepository.save(empresa);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaParceira> atualizarEmpresa(@PathVariable Integer id, @RequestBody EmpresaParceira empresa) {
        if (!empresaParceiraRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        if (empresa.getNome() == null || empresa.getEmail() == null || empresa.getCnpj() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        empresa.setId(id.longValue());
        EmpresaParceira empresaAtualizada = empresaParceiraRepository.save(empresa);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable Integer id) {
        if (!empresaParceiraRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        empresaParceiraRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}