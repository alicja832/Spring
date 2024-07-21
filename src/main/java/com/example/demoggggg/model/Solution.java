package com.example.demoggggg.model;
import jakarta.persistence.*;

@Entity
@Table(name = "solution")
public class Solution {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(unique = true,name="id" ,nullable = false)
    private int Id;
    private String solutionContent;
    @ManyToOne
    @JoinColumn(name="exercise_id", nullable=false)
    private Exercise exercise;
    @ManyToOne
    @JoinColumn(name="student_id", nullable=false)
    private Student student;
    private int score = 0;

    public String getSolutionContent() {
        return solutionContent;
    }

    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }

    public Solution() {
    }
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
}
