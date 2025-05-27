package com.projeto2.moedaEstudantil.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projeto2.moedaEstudantil.dto.request.AlunoDTO;
import com.projeto2.moedaEstudantil.dto.request.CadastroProfessorDTO;
import com.projeto2.moedaEstudantil.dto.request.DepartamentoDTO;
import com.projeto2.moedaEstudantil.dto.request.InstituicaoDTO;
import com.projeto2.moedaEstudantil.dto.response.InstituicaoListDTO;
import com.projeto2.moedaEstudantil.dto.response.DepartamentoListDTO;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.Curso;
import com.projeto2.moedaEstudantil.model.Departamento;
import com.projeto2.moedaEstudantil.model.Endereco;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.model.TipoTransacao;
import com.projeto2.moedaEstudantil.model.Transacao;

import com.projeto2.moedaEstudantil.model.enums.Role;
import com.projeto2.moedaEstudantil.repositories.CursoRepository;
import com.projeto2.moedaEstudantil.repositories.DepartamentoRepository;
import com.projeto2.moedaEstudantil.repositories.InstituicaoEnsinoRepository;
import com.projeto2.moedaEstudantil.repositories.ProfessorRepository;
import com.projeto2.moedaEstudantil.repositories.TransacaoRepository;
import com.projeto2.moedaEstudantil.repositories.UsuarioRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private CursoRepository cursoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private InstituicaoEnsinoRepository instituicaoRepository;

    @Autowired
    private DepartamentoRepository departamentoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void cadastrarAluno(AlunoDTO cadastroDTO) {
        if (usuarioRepository.findByEmail(cadastroDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        InstituicaoEnsino instituicao = instituicaoRepository.findById(cadastroDTO.getInstituicaoId())
            .orElseThrow(() -> new RuntimeException("Instituição de ensino não encontrada"));

        Curso curso = cursoRepository.findById(cadastroDTO.getCursoId())
            .orElseThrow(() -> new RuntimeException("Curso não encontrado"));

        Endereco endereco = new Endereco();
        endereco.setRua(cadastroDTO.getRua());
        endereco.setNumero(cadastroDTO.getNumero());
        endereco.setComplemento(cadastroDTO.getComplemento());
        endereco.setBairro(cadastroDTO.getBairro());
        endereco.setCidade(cadastroDTO.getCidade());
        endereco.setEstado(cadastroDTO.getEstado());

        Aluno aluno = new Aluno();
        aluno.setEmail(cadastroDTO.getEmail());
        aluno.setSenha(passwordEncoder.encode(cadastroDTO.getSenha()));
        aluno.setRole(Role.ALUNO);
        aluno.setNome(cadastroDTO.getNome());
        aluno.setCpf(cadastroDTO.getCpf());
        aluno.setRg(cadastroDTO.getRg());
        aluno.setEndereco(endereco);
        aluno.setInstituicaoEnsino(instituicao);
        aluno.setCurso(curso);

        usuarioRepository.save(aluno);
    }

    @Transactional
    public InstituicaoEnsino cadastrarInstituicao(InstituicaoDTO dto) {
        InstituicaoEnsino instituicao = new InstituicaoEnsino();
        instituicao.setNome(dto.getNome());
        instituicao.setCnpj(dto.getCnpj());
        return instituicaoRepository.save(instituicao);
    }

    public List<InstituicaoListDTO> listarInstituicoes() {
        List<InstituicaoEnsino> instituicoes = instituicaoRepository.findAll();
        return instituicoes.stream().map(this::toInstituicaoListDTO).collect(Collectors.toList());
    }

    private InstituicaoListDTO toInstituicaoListDTO(InstituicaoEnsino instituicao) {
        InstituicaoListDTO dto = new InstituicaoListDTO();
        dto.setId(instituicao.getId());
        dto.setNome(instituicao.getNome());
        dto.setCnpj(instituicao.getCnpj());
        
        if (instituicao.getDepartamentos() != null) {
            dto.setDepartamentos(instituicao.getDepartamentos().stream()
                .map(this::toDepartamentoListDTOForInstituicao)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private InstituicaoListDTO.DepartamentoListDTO toDepartamentoListDTOForInstituicao(Departamento departamento) {
        InstituicaoListDTO.DepartamentoListDTO dto = new InstituicaoListDTO.DepartamentoListDTO();
        dto.setId(departamento.getId());
        dto.setNome(departamento.getNome());
        
        if (departamento.getProfessores() != null) {
            dto.setProfessores(departamento.getProfessores().stream()
                .map(this::toProfessorListDTO)
                .collect(Collectors.toList()));
        }
        
        return dto;
    }

    private InstituicaoListDTO.ProfessorListDTO toProfessorListDTO(Professor professor) {
        InstituicaoListDTO.ProfessorListDTO dto = new InstituicaoListDTO.ProfessorListDTO();
        dto.setId(professor.getId());
        dto.setNome(professor.getNome());
        dto.setEmail(professor.getEmail());
        return dto;
    }

    @Transactional
    public Departamento cadastrarDepartamento(DepartamentoDTO dto) {
        InstituicaoEnsino instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        Departamento departamento = new Departamento();
        departamento.setNome(dto.getNome());
        departamento.setInstituicaoEnsino(instituicao);
        return departamentoRepository.save(departamento);
    }

    public List<Departamento> listarDepartamentosPorInstituicao(Integer instituicaoId) {
        return departamentoRepository.findByInstituicaoEnsinoId(instituicaoId);
    }

    @Transactional
    public Professor cadastrarProfessor(CadastroProfessorDTO dto) {
        InstituicaoEnsino instituicao = instituicaoRepository.findById(dto.getInstituicaoId())
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        Departamento departamento = departamentoRepository.findById(dto.getDepartamentoId())
                .orElseThrow(() -> new RuntimeException("Departamento não encontrado"));

        if (!departamento.getInstituicaoEnsino().getId().equals(instituicao.getId())) {
            throw new RuntimeException("O departamento não pertence à instituição informada");
        }

        Professor professor = new Professor();
        professor.setNome(dto.getNome());
        professor.setEmail(dto.getEmail());
        professor.setCpf(dto.getCpf());
        professor.setSenha(passwordEncoder.encode(dto.getSenha()));
        professor.setDepartamento(departamento);
        professor.setInstituicaoEnsino(instituicao);
        professor.setSaldoMoedas(100); // Inicia com 100 moedas
        professor.setRole(Role.PROFESSOR); // Define o papel como PROFESSOR

        return professorRepository.save(professor);
    }

    public List<DepartamentoListDTO> listarDepartamentosPorInstituicaoDTO(Integer instituicaoId) {
        InstituicaoEnsino instituicao = instituicaoRepository.findById(instituicaoId)
                .orElseThrow(() -> new RuntimeException("Instituição não encontrada"));

        return instituicao.getDepartamentos().stream()
                .map(this::toDepartamentoListDTO)
                .collect(Collectors.toList());
    }

    private DepartamentoListDTO toDepartamentoListDTO(Departamento departamento) {
        DepartamentoListDTO dto = new DepartamentoListDTO();
        dto.setId(departamento.getId());
        dto.setNome(departamento.getNome());
        return dto;
    }

    @Transactional
    public void distribuirMoedasParaProfessores() {
        List<Professor> professores = professorRepository.findAll();
        
        for (Professor professor : professores) {
            // Cria uma transação do tipo DISTRIBUICAO_MOEDAS
            Transacao transacao = new Transacao(
                null, // origem é null pois é o sistema
                professor, // destino é o professor
                100.0, // valor fixo de 100 moedas
                "Distribuição mensal de moedas", // motivo
                TipoTransacao.DISTRIBUICAO_MOEDAS // tipo
            );
            
            // Atualiza o saldo do professor
            professor.setSaldoMoedas(professor.getSaldoMoedas() + 100);
            
            // Salva as alterações
            professorRepository.save(professor);
            transacaoRepository.save(transacao);
        }
    }
} 