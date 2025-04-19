package com.projeto2.moedaEstudantil.security.cors;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuração global de CORS (Cross-Origin Resource Sharing) para a aplicação.
 * <p>
 * Permite requisições HTTP vindas do frontend hospedado em <code>http://localhost:</code>,
 * com os métodos GET e POST liberados para todos os endpoints.
 */

 @Configuration
 public class CorsConfig implements WebMvcConfigurer {
     @Override
     public void addCorsMappings(CorsRegistry registry) {
         registry.addMapping("/*")
                 .allowedOrigins("http://localhost:5173")
                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                 .allowedHeaders("")
                 .allowCredentials(true);
     }
 }