package com.example.pythonapp.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "student")
public class Student  extends UserEntity{

    @Column(name = "score", nullable = false, unique = true)
    private int score=0;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id")
    @JsonRawValue
    List<Solution> solutions;

    public Student()
    {
        solutions = new ArrayList<>();
    }
    public List<Solution> getSolutions() {
        
        return solutions;
    }
    public void updateSolution(Solution solution)
    {
        solutions.set(solutions.indexOf(solution),solution);
    }
    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }
    public void addSolution(Solution solution)
    {
        solutions.add(solution);
    }
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Student student = (Student) o;
        return this.id == student.id;
    }
    public void addPoints(int score){
        this.score+=score;
    }

}
