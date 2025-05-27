package com.projeto2.moedaEstudantil.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto2.moedaEstudantil.dto.response.AlunoInfoDTO;
import com.projeto2.moedaEstudantil.dto.response.AlunoResponseDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemResgateDTO;
import com.projeto2.moedaEstudantil.dto.response.TransacaoRecebimentoDTO;
import com.projeto2.moedaEstudantil.dto.response.VantagemResgatadaDTO;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.Transacao;
import com.projeto2.moedaEstudantil.model.TipoTransacao;
import com.projeto2.moedaEstudantil.model.Vantagem;
import com.projeto2.moedaEstudantil.repositories.AlunoRepository;
import com.projeto2.moedaEstudantil.repositories.TransacaoRepository;
import com.projeto2.moedaEstudantil.repositories.VantagemRepository;
import com.projeto2.moedaEstudantil.model.Professor;

@Service
public class AlunoService {

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private VantagemRepository vantagemRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public List<AlunoResponseDTO> listarAlunos() {
        List<Aluno> alunos = alunoRepository.findAll();
        return alunos.stream()
                .map(this::toAlunoResponseDTO)
                .collect(Collectors.toList());
    }

    private AlunoResponseDTO toAlunoResponseDTO(Aluno aluno) {
        AlunoResponseDTO dto = new AlunoResponseDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setCpf(aluno.getCpf());
        dto.setEmail(aluno.getEmail());
        dto.setRg(aluno.getRg());
        dto.setEndereco(aluno.getEndereco());
        dto.setCurso(aluno.getCurso().getNome());
        dto.setInstituicao(aluno.getInstituicaoEnsino().getNome());
        return dto;
    }

    public AlunoInfoDTO getAlunoInfo(Authentication authentication) {
        String email = authentication.getName();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        AlunoInfoDTO dto = new AlunoInfoDTO();
        dto.setId(aluno.getId());
        dto.setNome(aluno.getNome());
        dto.setEmail(aluno.getEmail());
        dto.setSaldoMoedas(aluno.getSaldoMoedas());
        dto.setCurso(aluno.getCurso().getNome());
        dto.setInstituicao(aluno.getInstituicaoEnsino().getNome());
        
        return dto;
    }

    @Transactional
    public VantagemResgateDTO resgatarVantagem(Integer vantagemId, Authentication authentication) {
        String email = authentication.getName();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        Vantagem vantagem = vantagemRepository.findById(vantagemId)
                .orElseThrow(() -> new RuntimeException("Vantagem não encontrada"));

        if (aluno.getSaldoMoedas() < vantagem.getValor()) {
            throw new RuntimeException("Saldo insuficiente para resgatar esta vantagem");
        }

        // Cria a transação de resgate
        Transacao transacao = Transacao.criarTransacaoVantagem(
            aluno, 
            vantagem.getEmpresaParceiraResponsavel(), 
            vantagem, 
            vantagem.getValor()
        );
        transacaoRepository.save(transacao);

        // Atualiza o saldo do aluno
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() - vantagem.getValor());
        aluno.getVantagensAdquiridas().add(vantagem);
        alunoRepository.save(aluno);

        // Retorna os dados do resgate
        return new VantagemResgateDTO(
            vantagem.getDescricao(),
            vantagem.getValor(),
            vantagem.getDesconto(),
            vantagem.getEmpresaParceiraResponsavel().getNome(),
            transacao.getCupomGerado(),
            aluno.getSaldoMoedas()
        );
    }

    public List<VantagemResgatadaDTO> listarVantagensResgatadas(Authentication authentication) {
        String email = authentication.getName();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        List<Transacao> transacoes = transacaoRepository.findByOrigemAndTipoOrderByDataDesc(aluno, TipoTransacao.RESGATE_VANTAGEM);
        
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
        dto.setImagemUrl(null);
        return dto;
    }

    public List<TransacaoRecebimentoDTO> listarRecebimentos(Authentication authentication) {
        String email = authentication.getName();
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));
        
        List<Transacao> transacoes = transacaoRepository.findByDestinoAndTipoOrderByDataDesc(aluno, TipoTransacao.ENVIO_MOEDAS);
        
        return transacoes.stream()
                .map(this::toTransacaoRecebimentoDTO)
                .collect(Collectors.toList());
    }

    private TransacaoRecebimentoDTO toTransacaoRecebimentoDTO(Transacao transacao) {
        TransacaoRecebimentoDTO dto = new TransacaoRecebimentoDTO();
        dto.setId(transacao.getId());
        Professor professor = (Professor) transacao.getOrigem();
        dto.setProfessorNome(professor.getNome());
        dto.setData(transacao.getData());
        dto.setMotivo(transacao.getMotivo());
        dto.setValor(transacao.getValor());
        return dto;
    }
}