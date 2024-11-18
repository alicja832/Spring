package com.example.pythonapp.dto;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Student;

public class LongSolutionDto {

    private String solutionContent;
    private String output;
    private int Id;
    private Exercise exercise;
    private Student student;
    private int score = 0;

    public int getId() {
        return Id;
    }
    public void setId(int id) {
        Id = id;
    }
    public Exercise getExercise() {
        return exercise;
    }
    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score = score;
    }
    public Student getStudent() {return student;}
    public void setStudent(Student student) {this.student = student;}

    public String getSolutionContent() {
        return solutionContent;
    }

    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }
    
    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }
}
