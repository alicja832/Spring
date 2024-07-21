package com.example.demoggggg.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="name",updatable=true)
    private String name;
    @Column(name="introduction",updatable=true)
    private String introduction;
    @Column(name="content",updatable=true)
    private String content;
    @Column(name="maxPoints",updatable=true)
    private int maxPoints;
    @Column(name="correctSolution",updatable=true)
    private String correctSolution;
    private String correctOutput = null;

    @OneToMany(mappedBy="exercise")
    List<Solution> solutions = null;

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

    public void addSolution(Solution solution) {
        if (solutions == null) {
            solutions = new ArrayList<>();
        }
        solutions.add(solution);
    }
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
    public String toString()
    {
        return String.valueOf(id);
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