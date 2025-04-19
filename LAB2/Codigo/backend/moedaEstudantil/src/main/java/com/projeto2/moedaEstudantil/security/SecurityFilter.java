package com.projeto2.moedaEstudantil.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.EmpresaParceira;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.model.Usuario;
import com.projeto2.moedaEstudantil.repositories.UsuarioRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Filtro de segurança que intercepta cada requisição para validar o token JWT.
 * Caso o token seja válido, autentica o usuário no contexto de segurança do Spring.
 */
@RequiredArgsConstructor
@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var token = this.recoverToken(request);
        var login = tokenService.validateToken(token);

        String servletPath = request.getServletPath();

        // Liberando todas as rotas de registro e login do filtro de autenticação
        if (servletPath.startsWith("/auth/register") || servletPath.equals("/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (login != null) {
            Usuario user = usuarioRepository.findByEmail(login).orElseThrow(() -> new RuntimeException("User Not Found"));

            // Define a role dinamicamente com base no tipo do usuário
            String role = "ROLE_ADMIN"; // Default
            if (user instanceof Aluno) {
                role = "ROLE_ALUNO";
            }
            if (user instanceof Professor) {
                role = "ROLE_PROFESSOR";
            }
            if (user instanceof EmpresaParceira) {
                role = "ROLE_EMPRESA_PARCEIRA";
            }

            var authorities = Collections.singletonList(new SimpleGrantedAuthority(role));

            var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Recupera o token JWT do cabeçalho Authorization da requisição.
     */
    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) return null;
        return authHeader.replace("Bearer ", ""); // Importante: espaço após "Bearer"
    }
}