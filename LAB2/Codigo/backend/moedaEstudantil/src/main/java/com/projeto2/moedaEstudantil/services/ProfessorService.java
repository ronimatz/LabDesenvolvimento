package com.projeto2.moedaEstudantil.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto2.moedaEstudantil.dto.request.EnvioMoedasDTO;
import com.projeto2.moedaEstudantil.dto.response.ProfessorInfoDTO;
import com.projeto2.moedaEstudantil.dto.response.TransacaoHistoricoDTO;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.model.Transacao;
import com.projeto2.moedaEstudantil.model.TipoTransacao;
import com.projeto2.moedaEstudantil.repositories.AlunoRepository;
import com.projeto2.moedaEstudantil.repositories.ProfessorRepository;
import com.projeto2.moedaEstudantil.repositories.TransacaoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private AlunoRepository alunoRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    public ProfessorInfoDTO getProfessorInfo(Authentication authentication) {
        String email = authentication.getName();
        Professor professor = professorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        
        return toProfessorInfoDTO(professor);
    }

    private ProfessorInfoDTO toProfessorInfoDTO(Professor professor) {
        ProfessorInfoDTO dto = new ProfessorInfoDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setEmail(professor.getEmail());
        dto.setSaldoMoedas(professor.getSaldoMoedas());

        if (professor.getDepartamento() != null) {
            ProfessorInfoDTO.DepartamentoInfo depInfo = new ProfessorInfoDTO.DepartamentoInfo();
            depInfo.setId(professor.getDepartamento().getId());
            depInfo.setNome(professor.getDepartamento().getNome());
            dto.setDepartamento(depInfo);
        }

        if (professor.getInstituicaoEnsino() != null) {
            ProfessorInfoDTO.InstituicaoInfo instInfo = new ProfessorInfoDTO.InstituicaoInfo();
            instInfo.setId(professor.getInstituicaoEnsino().getId());
            instInfo.setNome(professor.getInstituicaoEnsino().getNome());
            dto.setInstituicaoEnsino(instInfo);
        }

        return dto;
    }

    @Transactional
    public void enviarMoedas(EnvioMoedasDTO envioMoedasDTO, Authentication authentication) {
        Professor professor = professorRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        
        Aluno aluno = alunoRepository.findById(envioMoedasDTO.getAlunoId())
                .orElseThrow(() -> new RuntimeException("Aluno não encontrado"));

        if (professor.getSaldoMoedas() < envioMoedasDTO.getQuantidade()) {
            throw new RuntimeException("Saldo insuficiente");
        }

        // Atualiza os saldos
        professor.setSaldoMoedas(professor.getSaldoMoedas() - envioMoedasDTO.getQuantidade());
        aluno.setSaldoMoedas(aluno.getSaldoMoedas() + envioMoedasDTO.getQuantidade());

        // Cria a transação
        Transacao transacao = new Transacao(
            professor,                           // origem
            aluno,                              // destino
            envioMoedasDTO.getQuantidade().doubleValue(), // valor
            envioMoedasDTO.getMotivo(),         // motivo
            TipoTransacao.ENVIO_MOEDAS          // tipo
        );

        // Salva as alterações
        professorRepository.save(professor);
        alunoRepository.save(aluno);
        transacaoRepository.save(transacao);
    }

    public List<TransacaoHistoricoDTO> getHistoricoEnvios(Authentication authentication) {
        Professor professor = professorRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("Professor não encontrado"));
        
        List<Transacao> transacoes = transacaoRepository.findByOrigemAndTipoOrderByDataDesc(
            professor, 
            TipoTransacao.ENVIO_MOEDAS
        );
        
        return transacoes.stream()
                .map(this::toTransacaoHistoricoDTO)
                .collect(Collectors.toList());
    }

    private TransacaoHistoricoDTO toTransacaoHistoricoDTO(Transacao transacao) {
        TransacaoHistoricoDTO dto = new TransacaoHistoricoDTO();
        dto.setId(transacao.getId());
        
        // Como sabemos que é uma transação de ENVIO_MOEDAS, o destino é sempre um Aluno
        Aluno aluno = (Aluno) transacao.getDestino();
        dto.setAlunoNome(aluno.getNome());
        dto.setAlunoEmail(aluno.getEmail());
        
        dto.setQuantidade(transacao.getValor().intValue());
        dto.setMotivo(transacao.getMotivo());
        dto.setData(transacao.getData());
        return dto;
    }
}
