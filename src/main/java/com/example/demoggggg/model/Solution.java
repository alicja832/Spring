package com.example.demoggggg.model;
import jakarta.persistence.*;

@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true,name="id" ,nullable = false)
    private int Id;
    @Column(name="solutioncontent" ,nullable = false)
    private String solutionContent;

    @ManyToOne
    @JoinColumn(name="exercise_id", nullable=false)
    private Exercise exercise;

    @ManyToOne
    @JoinColumn(name="student_id")
    private Student student;

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
    public int getStudentId() {
            return student.getId();
        }
    public void setStudent(Student student) {
            this.student = student;
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
            return this.exercise.getId() == solution.getExercise().getId() && this.student.getId() == solution.getStudentId();
        }
}
