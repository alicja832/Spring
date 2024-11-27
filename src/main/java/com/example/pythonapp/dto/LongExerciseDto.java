package com.example.pythonapp.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

public class LongExerciseDto {

    private int id;
    private String name;
    private String introduction;
    private String content;
    private int maxPoints;
    private String[] correctSolutions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
    
    public String[] getCorrectSolutions(){
        return correctSolutions;
    }

    public void setCorrectSolution(String[] correctSolutions) {
        this.correctSolutions = correctSolutions;
    }

}