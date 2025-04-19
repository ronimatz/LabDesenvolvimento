package com.projeto2.moedaEstudantil.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.projeto2.moedaEstudantil.model.Aluno;
import com.projeto2.moedaEstudantil.model.EmpresaParceira;
import com.projeto2.moedaEstudantil.model.Professor;
import com.projeto2.moedaEstudantil.model.Usuario;



/**
 * Serviço responsável pela geração e validação de tokens JWT para autenticação de usuários.
 */
@Service
public class TokenService {

    @Value("${API_SECURITY_TOKEN_SECRET}")
    private String secret;


    //Os Getters e Setters são apenas para testes JUnit. Se não, o Caram morre do coração kkkk
    public String getSecret(){
        return secret;
    }
    
    public void setSecret(String secret){
        this.secret = secret;
    }

    /**
     * Gera um token JWT para o usuário autenticado.
     *
     * @param user objeto {@link Usuario} autenticado.
     * @return token JWT como {@link String}.
     * @throws RuntimeException se ocorrer falha na criação do token.
     */
    public String generationToken(Usuario user) {
    try {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        // Define a role do usuário
        String role = "ROLE_ADMIN";
        if (user instanceof Aluno) {
                role = "ROLE_ALUNO";
            }
            if (user instanceof Professor) {
                role = "ROLE_PROFESSOR";
            }
            if (user instanceof EmpresaParceira) {
                role = "ROLE_EMPRESA_PARCEIRA";
            }

        return JWT.create()
                .withIssuer("login-auth-api")
                .withSubject(user.getEmail())
                .withClaim("role", role) // Adiciona o claim role
                .withExpiresAt(this.generationExpirationDate())
                .sign(algorithm);

    } catch (JWTCreationException exception) {
        throw new RuntimeException("Error while authenticating", exception);
    }
}

    /**
     * Valida o token JWT recebido e retorna o e-mail do usuário se for válido.
     *
     * @param token token JWT como {@link String}.
     * @return e-mail do usuário autenticado ou {@code null} se inválido.
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("login-auth-api")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            return null;
        }
    }

    /**
     * Gera a data de expiração do token (2 horas a partir do momento atual).
     *
     * @return data de expiração como {@link Instant}.
     */
    private Instant generationExpirationDate() {
        return LocalDateTime.now()
                .plusHours(2)
                .toInstant(ZoneOffset.of("-03:00"));
    }


  
}