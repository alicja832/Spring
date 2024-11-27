package com.example.pythonapp.model;
import jakarta.persistence.*;

@Entity
public class LongExercise extends Exercise{


    @Column(name="correct_solution")
    private String correctSolution;

    public LongExercise() {}
    public LongExercise(String name, String introduction, String content, int maxPoints,String correctSolution) {
        super(name,introduction,content,maxPoints);
        this.correctSolution = correctSolution;
    }


    public String getCorrectSolution(){
        return correctSolution;
    }

    public void setCorrectSolution(String correctSolution) {
        this.correctSolution = correctSolution;
    }

}