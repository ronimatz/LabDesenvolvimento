package com.projeto2.moedaEstudantil.services;

import com.projeto2.moedaEstudantil.dto.request.AlunoDTO;
import com.projeto2.moedaEstudantil.dto.request.LoginDTO;
import com.projeto2.moedaEstudantil.dto.response.LoginResponseDTO;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.Curso;
import com.projeto2.moedaEstudantil.model.Endereco;
import com.projeto2.moedaEstudantil.model.InstituicaoEnsino;
import com.projeto2.moedaEstudantil.model.enums.Role;
import com.projeto2.moedaEstudantil.repositories.CursoRepository;
import com.projeto2.moedaEstudantil.repositories.InstituicaoEnsinoRepository;
import com.projeto2.moedaEstudantil.repositories.UsuarioRepository;
import com.projeto2.moedaEstudantil.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.projeto2.moedaEstudantil.model.Admin;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.model.Usuario;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final InstituicaoEnsinoRepository instituicaoEnsinoRepository;
    private final CursoRepository cursoRepository;


    public void cadastrarAluno(AlunoDTO cadastroDTO) {
        if (usuarioRepository.findByEmail(cadastroDTO.getEmail()).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        InstituicaoEnsino instituicao = instituicaoEnsinoRepository.findById(cadastroDTO.getInstituicaoId())
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

    public LoginResponseDTO login(LoginDTO loginDTO) {
        try {
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getEmail(), 
                loginDTO.getSenha()
            );

            Authentication authentication = authenticationManager.authenticate(authToken);
            Usuario user = (Usuario) authentication.getPrincipal();
            
            String token = tokenService.generationToken(user);
            String role = user.getRole().name();
            String nome = "";

            // Get the user's name based on their type
            if (user instanceof Admin) {
                nome = ((Admin) user).getNome();
            } else if (user instanceof Professor) {
                nome = ((Professor) user).getNome();
            } else if (user instanceof Aluno) {
                nome = ((Aluno) user).getNome();
            }

            return new LoginResponseDTO(token, role, nome);
        } catch (Exception e) {
            throw new RuntimeException("Credenciais inválidas", e);
        }
    }
} 