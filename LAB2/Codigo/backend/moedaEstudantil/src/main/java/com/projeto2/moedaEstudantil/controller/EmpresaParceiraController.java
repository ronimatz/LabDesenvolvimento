package com.projeto2.moedaEstudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto2.moedaEstudantil.dto.EmpresaParceiraDTO;
import com.projeto2.moedaEstudantil.dto.VantagemDTO;
import com.projeto2.moedaEstudantil.model.EmpresaParceira;
import com.projeto2.moedaEstudantil.model.Vantagem;
import com.projeto2.moedaEstudantil.services.EmpresaParceiraService;

@RestController
@RequestMapping("/empresas-parceiras")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraService empresaParceiraService;


    @PostMapping
    public ResponseEntity<?> cadastrarEmpresa(@RequestBody EmpresaParceiraDTO dto) {
        EmpresaParceira empresa = empresaParceiraService.cadastrarEmpresa(dto);
        return ResponseEntity.ok(empresa);
    }

    @PostMapping("/{empresaId}/vantagens")
    public ResponseEntity<?> cadastrarVantagem(@PathVariable Integer empresaId, @RequestBody VantagemDTO dto) {
        Vantagem vantagem = empresaParceiraService.cadastrarVantagemParaEmpresa(empresaId, dto);
        return ResponseEntity.ok(vantagem);
    }
}