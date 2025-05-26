package com.projeto2.moedaEstudantil.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.projeto2.moedaEstudantil.model.Admin;
import com.projeto2.moedaEstudantil.repositories.AdminRepository;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Verifica se já existe um admin
        if (adminRepository.count() == 0) {
            // Cria o admin padrão
            Admin admin = new Admin("admin@sistema.com", passwordEncoder.encode("admin123"));
            admin.setNome("Administrador");
            adminRepository.save(admin);
            System.out.println("Admin padrão criado com sucesso!");
            System.out.println("Email: admin@sistema.com");
            System.out.println("Senha: admin123");
        }
    }
} 