package com.example.pythonapp.model;
import jakarta.persistence.*;

@Entity
public class LongExercise extends Exercise{


    @Column(name="correct_solution")
    private String correctSolution;
    @Column(name="correct_output")
    private String correctOutput = null;

    public String getCorrectSolution() {
        return correctSolution;
    }

    public void setCorrectSolution(String correctSolution) {
        this.correctSolution = correctSolution;
    }

    public String getCorrectOutput() {
        return correctOutput;
    }

    public void setCorrectOutput(String correctOutput) {
        this.correctOutput = correctOutput;
    }

}