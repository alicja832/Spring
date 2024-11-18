package com.example.pythonapp.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class LongSolution extends Solution{
    @Column(name="solution_content" ,nullable = false)
    private String solutionContent;

    public String getSolutionContent() {
        return solutionContent;
    }

    public void setSolutionContent(String solutionContent) {
        this.solutionContent = solutionContent;
    }
}
