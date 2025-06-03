package com.projeto2.moedaEstudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projeto2.moedaEstudantil.dto.request.DepartamentoDTO;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.services.DepartamentoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/departamentos")
public class DepartamentoController {

    @Autowired
    private DepartamentoService departamentoService;

    @PostMapping
    public ResponseEntity<?> save(@Valid @RequestBody DepartamentoDTO departamentoDTO) {
        try {
            Departamento criado = departamentoService.save(departamentoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
