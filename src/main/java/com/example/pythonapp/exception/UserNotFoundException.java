package com.example.pythonapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Nie znaleziono użytkownika o podanych danych.")
public class UserNotFoundException extends RuntimeException {
    
    @Override
    public String getMessage()
    {
        return "Błędne dane logowania";
    }
}
