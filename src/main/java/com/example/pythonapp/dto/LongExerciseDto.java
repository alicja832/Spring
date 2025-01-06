package com.example.pythonapp.dto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Map;
public class LongExerciseDto {

    private int id;
    private boolean access;
    private String name;
    private String introduction;
    private String content;
    private int maxPoints;
    private String[] correctSolutions;
    private String solutionSchema;
    private String[] testData;
    private Integer[] points;

    public LongExerciseDto(){}
    public LongExerciseDto(int id,boolean access,String name,String introduction,String content,int maxPoints,String[] correctSolutions,String solutionSchema,String[] testData,Integer[] points) {
        this.id = id;
        this.access = access;
        this.name = name;
        this.introduction = introduction;
        this.content = content;
        this.maxPoints = maxPoints;
        this.correctSolutions = correctSolutions;
        this.solutionSchema = solutionSchema;
        this.testData = testData;
        this.points = points;
    }

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
    public boolean getAccess()
    {
        return this.access;
    }
    public void setAccess(boolean access)
    {
        this.access = access;
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

    public void setCorrectSolutions(String[] correctSolutions) {
        this.correctSolutions = correctSolutions;
    }
    public void setSolutionSchema(String solutionSchema) {
        this.solutionSchema = solutionSchema;
    }
    public String getSolutionSchema() {
        return this.solutionSchema;
    }
    public String[] getTestData() {
        return testData;
    }

    public void setTestData(String[] testData) {
        this.testData = testData;
    }
    public Integer[] getPoints() {
        return points;
    }

    public void setPoints(Integer[] points) {
        this.points = points;
    }
}