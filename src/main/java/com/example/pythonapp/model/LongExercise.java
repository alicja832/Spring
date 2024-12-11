package com.example.pythonapp.model;
import jakarta.persistence.*;

@Entity
public class LongExercise extends Exercise{


    @Column(name="correct_solution")
    private String correctSolution;
    @Column(name="solution_schema")
    private String solutionSchema;

    public LongExercise() {}
    public LongExercise(String name, String introduction, String content, int maxPoints,String correctSolution,String solutionSchema) {
        super(name,introduction,content,maxPoints);
        this.correctSolution = correctSolution;
         this.solutionSchema = solutionSchema;
    }

    public String getSolutionSchema() {
        return solutionSchema;
    }

    public void setSolutionSchema(String solutionSchema) {
        this.solutionSchema = solutionSchema;
    }

    public String getCorrectSolution(){
        return correctSolution;
    }

    public void setCorrectSolution(String correctSolution) {
        this.correctSolution = correctSolution;
    }

}