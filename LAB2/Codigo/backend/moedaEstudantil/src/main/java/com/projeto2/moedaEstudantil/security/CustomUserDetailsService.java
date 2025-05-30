package com.projeto2.moedaEstudantil.security;



import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;


import com.projeto2.moedaEstudantil.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

/**
 * Implementação personalizada de {@link UserDetailsService} utilizada pelo Spring Security.
 * <p>
 * Responsável por buscar os detalhes do usuário (e-mail e senha) no banco de dados
 * com base no e-mail informado durante o login.
 */
@RequiredArgsConstructor
@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository repository;

    /**
     * Carrega o usuário com base no e-mail informado.
     *
     * @param username o e-mail do usuário.
     * @return objeto {@link UserDetails} contendo o e-mail, senha e lista de authorities.
     * @throws UsernameNotFoundException se o usuário não for encontrado no repositório.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}