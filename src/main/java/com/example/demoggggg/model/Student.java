package com.example.demoggggg.model;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.*;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "student")
public class Student  extends UserEntity{
//    @Id
//    @GeneratedValue(strategy= GenerationType.IDENTITY)
//    @Column(unique = true, name = "id",nullable = false)
//    private int id;
//    private String name;
//
//    @Column(unique = true)
//    private String email;
//    private String password;
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

        solutions.set( solutions.indexOf(solution),solution);
    }
    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }
    public void addSolution(Solution solution)
    {
        solutions.add(solution);
    }
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

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
