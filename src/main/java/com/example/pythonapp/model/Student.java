package com.example.pythonapp.model;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
public class Student  extends UserEntity{

    @Column(name = "score", nullable = false)
    private int score=0;

    public Student()
    {

    }
    public Student(UserEntity userEntity)
    {
        name = userEntity.name;
        email = userEntity.email;
        password = userEntity.password;
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

}
