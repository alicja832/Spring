package com.example.pythonapp.exception;

public class SolutionNotFoundException extends RuntimeException {
  public SolutionNotFoundException(String message) {
    super(message);
  }
}
