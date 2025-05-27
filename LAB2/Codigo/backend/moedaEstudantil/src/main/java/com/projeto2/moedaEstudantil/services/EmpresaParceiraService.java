package com.projeto2.moedaEstudantil.services;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto2.moedaEstudantil.dto.request.EmpresaParceiraDTO;

import com.projeto2.moedaEstudantil.dto.response.VantagemDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemResgatadaDTO;
import com.projeto2.moedaEstudantil.model.EmpresaParceira;
import com.projeto2.moedaEstudantil.model.Vantagem;
import com.projeto2.moedaEstudantil.model.Transacao;
import com.projeto2.moedaEstudantil.model.TipoTransacao;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.enums.Role;
import com.projeto2.moedaEstudantil.repositories.EmpresaParceiraRepository;
import com.projeto2.moedaEstudantil.repositories.VantagemRepository;
import com.projeto2.moedaEstudantil.repositories.TransacaoRepository;

@Service
public class EmpresaParceiraService {

    @Autowired
    private EmpresaParceiraRepository empresaParceiraRepository;

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Transactional
    public EmpresaParceira cadastrarEmpresa(EmpresaParceiraDTO dto) {
        if (empresaParceiraRepository.findByNome(dto.getNome()).isPresent()) {
            throw new RuntimeException("Empresa com o nome '" + dto.getNome() + "' já cadastrada.");
        }

        EmpresaParceira empresa = new EmpresaParceira(dto.getEmail(), passwordEncoder.encode(dto.getSenha()), dto.getNome(), dto.getCnpj());

        if (empresa.getVantagens() == null) {
            empresa.setVantagens(new ArrayList<>());
        }

        if (dto.getVantagens() != null && !dto.getVantagens().isEmpty()) {
            for (VantagemDTO vantagemDTO : dto.getVantagens()) {
                boolean descricaoJaExiste = empresa.getVantagens().stream()
                        .anyMatch(v -> v.getDescricao().equalsIgnoreCase(vantagemDTO.getDescricao()));

                if (descricaoJaExiste) {
                    throw new RuntimeException("Vantagem com a descrição '" + vantagemDTO.getDescricao()
                            + "' já adicionada para esta empresa.");
                }

                Vantagem vantagem = new Vantagem();
                vantagem.setDescricao(vantagemDTO.getDescricao());
                vantagem.setValor(vantagemDTO.getValor());
                vantagem.setDesconto(vantagemDTO.getDesconto());

                if (vantagemDTO.getFotoProduto() != null && !vantagemDTO.getFotoProduto().isEmpty()) {
                    byte[] fotoBytes = Base64.getDecoder().decode(vantagemDTO.getFotoProduto());
                    vantagem.setFotoProduto(fotoBytes);
                }

                vantagem.setEmpresaParceiraResponsavel(empresa);
                empresa.getVantagens().add(vantagem);
            }
        }

        return empresaParceiraRepository.save(empresa);
    }

    public List<EmpresaParceira> listarTodas() {
        return empresaParceiraRepository.findAll();
    }

    public EmpresaParceira buscarPorId(Integer id) {
        return empresaParceiraRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o ID: " + id));
    }

    @Transactional
    public EmpresaParceira editarEmpresa(Integer id, EmpresaParceiraDTO dto) {
        EmpresaParceira empresa = buscarPorId(id);

        if (!empresa.getNome().equals(dto.getNome()) && empresaParceiraRepository.findByNome(dto.getNome()).isPresent()) {
            throw new RuntimeException("Empresa com o nome '" + dto.getNome() + "' já cadastrada.");
        }

        empresa.setEmail(dto.getEmail());
        empresa.setSenha(passwordEncoder.encode(dto.getSenha()));
        empresa.setNome(dto.getNome());
        empresa.setCnpj(dto.getCnpj());

        return empresaParceiraRepository.save(empresa);
    }

    @Transactional
    public Vantagem adicionarVantagem(Integer empresaId, VantagemDTO vantagemDTO) {
        EmpresaParceira empresa = buscarPorId(empresaId);

        boolean descricaoJaExiste = empresa.getVantagens().stream()
                .anyMatch(v -> v.getDescricao().equalsIgnoreCase(vantagemDTO.getDescricao()));

        if (descricaoJaExiste) {
            throw new RuntimeException("Vantagem com a descrição '" + vantagemDTO.getDescricao()
                    + "' já adicionada para esta empresa.");
        }

        Vantagem novaVantagem = new Vantagem();
        novaVantagem.setDescricao(vantagemDTO.getDescricao());
        novaVantagem.setValor(vantagemDTO.getValor());
        novaVantagem.setDesconto(vantagemDTO.getDesconto());

        if (vantagemDTO.getFotoProduto() != null && !vantagemDTO.getFotoProduto().isEmpty()) {
            byte[] fotoBytes = Base64.getDecoder().decode(vantagemDTO.getFotoProduto());
            novaVantagem.setFotoProduto(fotoBytes);
        }

        novaVantagem.setEmpresaParceiraResponsavel(empresa);
        empresa.getVantagens().add(novaVantagem);

        return vantagemRepository.save(novaVantagem);
    }

    @Transactional
    public Vantagem editarVantagem(Integer vantagemId, VantagemDTO vantagemDTO) {
        Vantagem vantagem = vantagemRepository.findById(vantagemId)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada com o ID: " + vantagemId));

        vantagem.setDescricao(vantagemDTO.getDescricao());
        vantagem.setValor(vantagemDTO.getValor());
        vantagem.setDesconto(vantagemDTO.getDesconto());

        if (vantagemDTO.getFotoProduto() != null && !vantagemDTO.getFotoProduto().isEmpty()) {
            byte[] fotoBytes = Base64.getDecoder().decode(vantagemDTO.getFotoProduto());
            vantagem.setFotoProduto(fotoBytes);
        } else if (vantagemDTO.getFotoProduto() == null) {
            vantagem.setFotoProduto(null); // Permite remover a foto
        }

        return vantagemRepository.save(vantagem);
    }

    @Transactional
    public void removerVantagem(Integer empresaId, Integer vantagemId) {
        EmpresaParceira empresa = buscarPorId(empresaId);
        boolean removido = empresa.getVantagens().removeIf(vantagem -> vantagem.getId().equals(vantagemId));
        if (removido) {
            empresaParceiraRepository.save(empresa); // Salva a empresa após remover a vantagem da lista
            vantagemRepository.deleteById(vantagemId); // Remove a vantagem do banco de dados
        } else {
            throw new RuntimeException("Vantagem com ID '" + vantagemId + "' não encontrada para a empresa com ID '" + empresaId + "'.");
        }
    }

    @Transactional
    public void deletarEmpresa(Integer id) {
        empresaParceiraRepository.deleteById(id);
    }

    public void cadastrarEmpresaParceira(EmpresaParceiraDTO dto) {
        if (empresaParceiraRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email já cadastrado");
        }

        if (empresaParceiraRepository.existsByCnpj(dto.getCnpj())) {
            throw new RuntimeException("CNPJ já cadastrado");
        }

        EmpresaParceira empresaParceira = new EmpresaParceira();
        empresaParceira.setEmail(dto.getEmail());
        empresaParceira.setSenha(passwordEncoder.encode(dto.getSenha()));
        empresaParceira.setCnpj(dto.getCnpj());
        empresaParceira.setNome(dto.getNome());
        empresaParceira.setRole(Role.EMPRESA_PARCEIRA);

        empresaParceiraRepository.save(empresaParceira);
    }

    public EmpresaParceira buscarPorEmail(String email) {
        return empresaParceiraRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada com o email: " + email));
    }

    public List<VantagemResgatadaDTO> listarVantagensCompradas(Integer empresaId) {
        EmpresaParceira empresa = buscarPorId(empresaId);
        
        List<Transacao> transacoes = transacaoRepository.findByDestinoAndTipoOrderByDataDesc(empresa, TipoTransacao.RESGATE_VANTAGEM);
        
        return transacoes.stream()
                .map(this::toVantagemResgatadaDTO)
                .collect(Collectors.toList());
    }

    private VantagemResgatadaDTO toVantagemResgatadaDTO(Transacao transacao) {
        VantagemResgatadaDTO dto = new VantagemResgatadaDTO();
        dto.setId(transacao.getId());
        Vantagem vantagem = transacao.getVantagem();
        dto.setVantagemDescricao(vantagem.getDescricao());
        dto.setEmpresaNome(vantagem.getEmpresaParceiraResponsavel().getNome());
        dto.setValor(transacao.getValor());
        dto.setData(transacao.getData());
        dto.setCupomGerado(transacao.getCupomGerado());
        
        // Adiciona o nome do aluno que comprou a vantagem
        Aluno aluno = (Aluno) transacao.getOrigem();
        dto.setAlunoNome(aluno.getNome());
        
        // Adiciona a URL da imagem se disponível
        if (vantagem.getFotoProduto() != null) {
            dto.setImagemUrl("data:image/jpeg;base64," + Base64.getEncoder().encodeToString(vantagem.getFotoProduto()));
        }
        
        return dto;
    }
}