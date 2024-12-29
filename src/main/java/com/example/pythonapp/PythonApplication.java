package com.example.pythonapp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PythonApplication {
    
   
    public static final String frontend = "https://pythfront.azurewebsites.net/";
    public static void main(String[] args) {
       
        SpringApplication.run(PythonApplication.class, args);
    }
}
