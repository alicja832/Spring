package com.example.pythonapp.service;

import com.example.pythonapp.model.*;

import java.util.List;

public interface SolutionService {
    Solution save(ShortSolution solution);
    Solution save(LongSolution solution);
    List<Solution> findStudentSolution(Student student);
    Solution findById(int id);
    void update(int id, LongSolution solution);
    int deleteById(int id);
    List<Solution> getAllSolutionsByExercise(Exercise exercise);
}
