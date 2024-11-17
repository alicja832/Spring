package com.example.pythonapp.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "solution")
@Inheritance(strategy = InheritanceType.JOINED)
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true,name="id" ,nullable = false)
    private int Id;
    @ManyToOne
    @JoinColumn(name="exercise_id", nullable=false)
    private Exercise exercise;
    @ManyToOne
    @JoinColumn(name="student_id", nullable=false)
    @JsonIgnore
    private Student student;
    private int score = 0;  

    public Solution() {}
    public int getId() {
            return Id;
        }
    public void setId(int id) {
            Id = id;
        }
    public Exercise getExercise() {
            return exercise;
        }
    public void setExercise(Exercise exercise) {
            this.exercise = exercise;
        }
    public int getScore() {
            return score;
        }
    public void setScore(int score) {
            this.score = score;
        }
    public Student getStudent() {return student;}
    public void setStudent(Student student) {this.student = student;}

    public boolean equals(Object o) {
        if (this == o)
                return true;
        if (o == null)
                return false;
        if (getClass() != o.getClass())
                return false;
        Solution solution = (Solution) o;
        return (this.exercise.getId() == solution.getExercise().getId()) && ( this.student.getId() == (solution.student.getId()));
    }
}
