
package com.example.pythonapp.dto;


import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Teacher;

import java.util.Collection;

public class ExerciseDto {

    private String name;

    private String introduction;

    private String content;

    private int maxPoints;

    private String correctSolution;

    private String correctOutput = null;

    private Collection<Solution> solutions = null;

    private Teacher teacher;

    public String getCorrectOutput() {
        return correctOutput;
    }

    public void setCorrectOutput(String correctOutput) {
        this.correctOutput = correctOutput;
    }

    public ExerciseDto() {
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public String getCorrectSolution() {
        return correctSolution;
    }

    public void setCorrectSolution(String correctSolution) {
        this.correctSolution = correctSolution;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }

    public String getContent() { return content;}
    public void setContent(String content) {
        this.content = content;
    }

    public Collection<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(Collection<Solution> solutions) {
        this.solutions = solutions;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
}