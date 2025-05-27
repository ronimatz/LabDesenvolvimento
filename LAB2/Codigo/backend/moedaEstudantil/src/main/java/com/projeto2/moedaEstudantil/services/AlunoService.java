package com.projeto2.moedaEstudantil.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.projeto2.moedaEstudantil.dto.AlunoDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoResponseDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoInfoDTO;
import com.projeto2.moedaEstudantil.dto.response.TransacaoRecebimentoDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemAdquiridaDTO;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.Curso;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.model.Extrato;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.model.Transacao;
import com.projeto2.moedaEstudantil.model.VantagemAdquirida;
import com.projeto2.moedaEstudantil.repositories.AlunoRepository;
import com.projeto2.moedaEstudantil.repositories.CursoRepository;
import com.projeto2.moedaEstudantil.repositories.TransacaoRepository;
import com.projeto2.moedaEstudantil.repositories.VantagemAdquiridaRepository;
import com.projeto2.moedaEstudantil.repositories.InstituicaoEnsinoRepository;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private InstituicaoEnsinoRepository instituicaoRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private VantagemAdquiridaRepository vantagemAdquiridaRepository;

    public AlunoResponseDTO criarAluno(AlunoDTO dto) {
    if (alunoRepository.existsByCpf(dto.getCpf())) {
        throw new IllegalArgumentException("CPF já cadastrado");
    }

    if (alunoRepository.existsByRg(dto.getRg())) {
        throw new IllegalArgumentException("RG já cadastrado");
    }

    InstituicaoEnsino instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
            .orElseThrow(() -> new IllegalArgumentException("Instituição não encontrada"));

    Curso curso = cursoRepository.findById(dto.getCursoId())
            .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));

    Departamento departamento = curso.getDepartamento();
    if (departamento == null || departamento.getInstituicaoEnsino() == null) {
        throw new IllegalArgumentException("Curso não está associado a uma instituição válida");
    }

    if (!departamento.getInstituicaoEnsino().getId().equals(instituicao.getId())) {
        throw new IllegalArgumentException("Curso não pertence à instituição informada");
    }

    Aluno aluno = new Aluno(
            dto.getEmail(),
            dto.getSenha(),
            dto.getNome(),
            dto.getCpf(),
            dto.getRg(),
            dto.getEndereco(),
            instituicao,
            curso
    );

    Extrato extrato = new Extrato();
    extrato.setUsuario(aluno);
    extrato.setSaldoMoedas(0);
    aluno.setExtrato(extrato);

    aluno = alunoRepository.save(aluno);

    return mapToResponseDTO(aluno);
}
    public List<AlunoResponseDTO> listarTodos() {
        return alunoRepository.findAll().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
    }

    public AlunoResponseDTO buscarPorId(Integer id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
        return mapToResponseDTO(aluno);
    }

    public void deletarAluno(Integer id) {
        if (!alunoRepository.existsById(id)) {
            throw new IllegalArgumentException("Aluno não encontrado");
        }
        alunoRepository.deleteById(id);
    }

    public AlunoResponseDTO editarAluno(Integer id, AlunoDTO dto) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));
    
        
        if (!aluno.getCpf().equals(dto.getCpf()) && alunoRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("CPF já cadastrado");
        }
    
        if (!aluno.getRg().equals(dto.getRg()) && alunoRepository.existsByRg(dto.getRg())) {
            throw new IllegalArgumentException("RG já cadastrado");
        }
    
        
        aluno.setEmail(dto.getEmail());
        aluno.setSenha(dto.getSenha());
        aluno.setNome(dto.getNome());
        aluno.setCpf(dto.getCpf());
        aluno.setRg(dto.getRg());
        aluno.setEndereco(dto.getEndereco());
    
        aluno = alunoRepository.save(aluno);
    
        return mapToResponseDTO(aluno);
    }
    
    private AlunoResponseDTO mapToResponseDTO(Aluno aluno) {
        return new AlunoResponseDTO(
                aluno.getId().intValue(),
                aluno.getNome(),
                aluno.getCpf(),
                aluno.getEmail(),
                aluno.getRg(),
                aluno.getEndereco(),
                aluno.getCurso().getNome(),
                aluno.getInstituicaoEnsino().getNome());
    }

    public AlunoInfoDTO getInfo(Authentication authentication) {
        String email = authentication.getName();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        return AlunoInfoDTO.builder()
                .nome(aluno.getNome())
                .saldoMoedas(aluno.getExtrato().getSaldoMoedas())
                .build();
    }

    public List<TransacaoRecebimentoDTO> getHistoricoRecebimentos(Authentication authentication) {
        String email = authentication.getName();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        List<Transacao> transacoes = transacaoRepository.findByAlunoOrderByDataDesc(aluno);

        return transacoes.stream()
                .map(transacao -> TransacaoRecebimentoDTO.builder()
                        .professorNome(transacao.getProfessor().getNome())
                        .data(transacao.getData())
                        .motivo(transacao.getMotivo())
                        .quantidade(transacao.getQuantidade())
                        .build())
                .collect(Collectors.toList());
    }

    public List<VantagemAdquiridaDTO> getVantagensAdquiridas(Authentication authentication) {
        String email = authentication.getName();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Aluno não encontrado"));

        List<VantagemAdquirida> vantagens = vantagemAdquiridaRepository.findByAlunoOrderByDataAquisicaoDesc(aluno);

        return vantagens.stream()
                .map(vantagem -> VantagemAdquiridaDTO.builder()
                        .empresa(vantagem.getVantagem().getEmpresa().getNome())
                        .descricao(vantagem.getVantagem().getDescricao())
                        .dataAquisicao(vantagem.getDataAquisicao())
                        .custo(vantagem.getVantagem().getCusto())
                        .status(vantagem.getStatus().toString())
                        .build())
                .collect(Collectors.toList());
    }
}