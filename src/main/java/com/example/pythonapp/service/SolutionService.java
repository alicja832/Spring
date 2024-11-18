package com.example.pythonapp.service;

import com.example.pythonapp.model.*;

import java.util.List;

public interface SolutionService {
    Solution save(ShortSolution solution);
    Solution save(LongSolution solution);
    List<Solution> findStudentSolution(Student student);
    Solution findById(int id);
    LongSolution findLongSolutionById(int id);
    int deleteById(int id);
    void updateSolution(int id, LongSolution solution);
    List<Solution> getAllSolutionsByExercise(Exercise exercise);
}
