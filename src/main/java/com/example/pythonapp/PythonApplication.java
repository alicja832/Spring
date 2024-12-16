package com.example.pythonapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Value;
@SpringBootApplication
public class PythonApplication {
    
   
    public static final String frontend ="http://localhost:3000";
    public static void main(String[] args) {
       
        SpringApplication.run(PythonApplication.class, args);
    }
}
