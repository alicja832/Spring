package com.example.pythonapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Solution not found")
public class SolutionNotFoundException extends RuntimeException {
    @Override
    public String getMessage()
    {
        return "Nie znaleziono podanego rozwiązania";
    }
}
