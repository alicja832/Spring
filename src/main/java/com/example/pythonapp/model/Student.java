package com.example.pythonapp.model;
import jakarta.persistence.*;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "student")
public class Student  extends UserEntity{

    @Column(name = "score", nullable = false)
    private int score=0;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "solution_id")
    List<Solution> solutions = new ArrayList<>();

    public Student()
    {

    }
    public Student(UserEntity userEntity)
    {
        name = userEntity.name;
        email = userEntity.email;
        password = userEntity.password;
    }
    public List<Solution> getSolutions() {
        
        return solutions;
    }

    public void addSolution(Solution solution)
    {
        solutions.add(solution);
    }
    public int getScore() {
        return score;
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
