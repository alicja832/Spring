package com.example.pythonapp.model;

import java.io.Serializable;

public class LongCorrectSolutionPartId implements Serializable {
    private Exercise exercise;
    private int order;
    
    public LongCorrectSolutionPartId()
    {}
    
    public LongCorrectSolutionPartId(Exercise exercise, int order) {
        this.exercise = exercise;
        this.order = order;
    }

    public Exercise getExercise_id() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
    @Override
    public boolean equals(Object other)
    {
       LongCorrectSolutionPartId to_compare = (LongCorrectSolutionPartId)other;
       return (to_compare.order == this.order) && (to_compare.exercise == this.exercise);
    }
   
}
