package com.projeto2.moedaEstudantil.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.projeto2.moedaEstudantil.model.Usuario;
import com.projeto2.moedaEstudantil.model.enums.Role;
import com.projeto2.moedaEstudantil.repository.UsuarioRepository;
import com.projeto2.moedaEstudantil.security.JwtTokenProvider;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail());
        
        if (usuario != null && usuario.getSenha().equals(request.getSenha())) {
            String token = jwtTokenProvider.createToken(usuario.getEmail(), usuario.getRole());
            return ResponseEntity.ok(new LoginResponse(token, usuario));
        }
        
        return ResponseEntity.badRequest().body("Credenciais inválidas");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()) != null) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        Usuario usuario = new Usuario(request.getEmail(), request.getSenha());
        usuario.setRole(request.getRole());
        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }
}

class LoginRequest {
    private String email;
    private String senha;

    public String getEmail() { return email; }
    public String getSenha() { return senha; }
}

class RegisterRequest {
    private String email;
    private String senha;
    private Role role;

    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public Role getRole() { return role; }
}

class LoginResponse {
    private String token;
    private Usuario usuario;

    public LoginResponse(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() { return token; }
    public Usuario getUsuario() { return usuario; }
}
