
package com.example.demoggggg.dto;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class TeacherDto {

    private int id;
    private String name;
    private String email;
    private String password;


    private List<Exercise> exercises = new ArrayList<>();

    public TeacherDto() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
