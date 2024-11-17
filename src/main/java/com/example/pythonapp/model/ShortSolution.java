package com.example.pythonapp.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ShortSolution extends Solution{
    @Column(name="answer" ,nullable = false)
    private char answer;

    public char getAnswer() {
        return answer;
    }

    public void setAnswer(char answer) {
        this.answer = answer;
    }
}
