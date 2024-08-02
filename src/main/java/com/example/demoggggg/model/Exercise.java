package com.example.demoggggg.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "exercise")
public class Exercise implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id",nullable = false)
    private int id;
    @Column(unique = true,name="name",updatable=true)
    private String name;
    @Column(unique = true,name="introduction",updatable=true)
    private String introduction;
    @Column(unique = true,name="content",updatable=true)
    private String content;
    @Column(name="maxpoints",updatable=true)
    private int maxPoints;
    @Column(name="correctsolution",updatable=true)
    private String correctSolution;
    @Column(name="correctoutput",updatable=true)
    private String correctOutput = null;

//    @OneToMany
//    @JoinColumn(name = "exercise_id")
//    private List<Solution> solutions = null;
    @ManyToOne
    @JoinColumn(name="teacher_id", nullable=false)
    @JsonIgnore
    private Teacher teacher;

//    public List<Solution> getSolutions() {
//        return solutions;
//    }
//
//    public void setSolutions(List<Solution> solutions) {
//        this.solutions = solutions;
//    }



    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getCorrectOutput() {
        return correctOutput;
    }

    public void setCorrectOutput(String correctOutput) {
        this.correctOutput = correctOutput;
    }
    public Exercise() {
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

//    public void addSolution(Solution solution) {
//        if (solutions == null) {
//            solutions = new ArrayList<>();
//        }
//        solutions.add(solution);
//    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getContent() { return content;}
    public void setContent(String content) {
        this.content = content;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Exercise exercise = (Exercise) o;
        return this.id == exercise.id;
    }
}