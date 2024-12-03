package com.example.pythonapp.model;
import jakarta.persistence.*;

@Entity
@Table(name = "solution_part")
@IdClass(LongCorrectSolutionPartId.class)
public class LongCorrectSolutionPart{

    @Id
    @Column(name="order_id")
    int order;
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id")
    Exercise exercise;
  
    @Column(name = "correct_solution_part")
    String correctSolutionPart;
    public LongCorrectSolutionPart(){}
    public LongCorrectSolutionPart(int order,  String correctSolutionPart){
        this.order = order;
        this.correctSolutionPart = correctSolutionPart;
    }
    public String getCorrectSolutionPart() {
        return correctSolutionPart;
    }

    public void setCorrectSolutionPart(String correctSolutionPart) {
        this.correctSolutionPart = correctSolutionPart;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
