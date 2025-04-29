package com.projeto2.moedaEstudantil.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto2.moedaEstudantil.dto.EmpresaParceiraDTO;
import com.projeto2.moedaEstudantil.dto.VantagemDTO;
import com.projeto2.moedaEstudantil.model.EmpresaParceira;
import com.projeto2.moedaEstudantil.model.Vantagem;
import com.projeto2.moedaEstudantil.repositories.EmpresaParceiraRepository;
import com.projeto2.moedaEstudantil.repositories.VantagemRepository;

@Service
public class EmpresaParceiraService {
    
    @Autowired
    private EmpresaParceiraRepository empresaParceiraRepository;
    @Autowired
    private VantagemRepository vantagemRepository;
    
    @Transactional
    public EmpresaParceira cadastrarEmpresa(EmpresaParceiraDTO dto) {
        
        if (empresaParceiraRepository.findByNome(dto.getNome()).isPresent()) {
            throw new RuntimeException("Empresa com o nome '" + dto.getNome() + "' já cadastrada.");
        }
          
        EmpresaParceira empresa = new EmpresaParceira(dto.getEmail(), dto.getSenha(), dto.getNome(), dto.getCnpj());

        if (dto.getVantagens() != null && !dto.getVantagens().isEmpty()) {
            for (VantagemDTO vantagemDTO : dto.getVantagens()) {
                
                boolean descricaoJaExiste = empresa.getVantagens().stream()
                        .anyMatch(v -> v.getDescricao().equalsIgnoreCase(vantagemDTO.getDescricao()));
                if (descricaoJaExiste) {
                    throw new RuntimeException("Vantagem com a descrição '" + vantagemDTO.getDescricao() + "' já adicionada para esta empresa.");
                }

                Vantagem vantagem = new Vantagem();
                vantagem.setDescricao(vantagemDTO.getDescricao());
                vantagem.setValor(vantagemDTO.getValor());
                vantagem.setDesconto(vantagemDTO.getDesconto());
                vantagem.setFotoProduto(vantagemDTO.getFotoProduto());
                vantagem.setEmpresaParceiraResponsavel(empresa);

                empresa.getVantagens().add(vantagem);
            }
        }

        return empresaParceiraRepository.save(empresa);
    }

    @Transactional
    public Vantagem cadastrarVantagemParaEmpresa(Integer empresaId, VantagemDTO dto) {
        EmpresaParceira empresa = empresaParceiraRepository.findById(empresaId)
            .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

       
        boolean descricaoJaExiste = empresa.getVantagens().stream()
            .anyMatch(v -> v.getDescricao().equalsIgnoreCase(dto.getDescricao()));
        if (descricaoJaExiste) {
            throw new RuntimeException("Já existe uma vantagem com a descrição '" + dto.getDescricao() + "' para esta empresa.");
        }

        Vantagem vantagem = new Vantagem();
        vantagem.setDescricao(dto.getDescricao());
        vantagem.setValor(dto.getValor());
        vantagem.setDesconto(dto.getDesconto());
        vantagem.setFotoProduto(dto.getFotoProduto());
        vantagem.setEmpresaParceiraResponsavel(empresa);

        
        vantagem = vantagemRepository.save(vantagem);

        
        empresa.getVantagens().add(vantagem);
        empresaParceiraRepository.save(empresa);

        return vantagem;
    }
}
