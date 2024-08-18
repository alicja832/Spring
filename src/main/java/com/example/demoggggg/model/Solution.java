package com.example.demoggggg.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.persistence.*;

@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true,name="id" ,nullable = false)
    private int Id;

    @Column(name="solutioncontent" ,nullable = false)
    private String solutionContent;

    @ManyToOne
    @JoinColumn(name="exercise_id", nullable=false)
    private Exercise exercise;
    private String studentEmail;
    private int score = 0;
    private String output = null;

    public String getOutput() {
        return output;
    }
    public void setOutput(String output) {
        this.output = output;
    }
    public String getSolutionContent() {
        return solutionContent;
    }
    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }
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
    public String getStudentEmail() {
            return studentEmail;
        }
    public void setStudentEmail(String studentEmail) {
            this.studentEmail = studentEmail;
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
            Solution solution = (Solution) o;
            return (this.exercise.getId() == solution.getExercise().getId()) && ( this.studentEmail.equals(solution.studentEmail));
    }
}
