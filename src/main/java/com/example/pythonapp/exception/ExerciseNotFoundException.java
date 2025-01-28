package com.example.pythonapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Exercise not found")
public class ExerciseNotFoundException extends RuntimeException {
    
    @Override
    public String getMessage()
    {
        return "Nie znaleziono zadania";
    }
}
