package com.example.pythonapp.model;
import jakarta.persistence.*;

@Entity
@Table(name = "testing_data")
public class TestingData{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, name = "id",nullable = false)
    private int id;
  
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "exercise_id",nullable = false)
    Exercise exercise;
  
    @Column(name = "test_data",nullable = false)
    String testData;

    @Column(name = "points",nullable = false)
    int points;

    public TestingData(){}
    public TestingData(Exercise exercise,int points, String test_data){
        this.points = points;
        this.testData = test_data;
        this.exercise = exercise;
    }
    public int getId()
    {
        return id;
    }
    public String getTestingData() {
        return testData;
    }

    public void setTestingData(String testData) {
        this.testData = testData;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public void setExercise(Exercise exercise) {
        this.exercise = exercise;
    }
}
