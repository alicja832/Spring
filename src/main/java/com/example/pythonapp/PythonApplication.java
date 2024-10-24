package com.example.pythonapp;

import org.python.core.PyString;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.python.core.PySystemState;
import org.python.core.Py;

@SpringBootApplication
public class PythonApplication {

    public static void main(String[] args) {

        SpringApplication.run(PythonApplication.class, args);
    }
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/").allowedOrigins("http://localhost:3000");
            }
        };
    }
}
