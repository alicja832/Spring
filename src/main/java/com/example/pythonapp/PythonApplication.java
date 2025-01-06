package com.example.pythonapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PythonApplication {
    
   
    // public static final String frontend = "https://naukapython.azurewebsites.net/";
     public static final String frontend = "http://localhost:3000";
    public static void main(String[] args) {
       
        SpringApplication.run(PythonApplication.class, args);
    }
}
