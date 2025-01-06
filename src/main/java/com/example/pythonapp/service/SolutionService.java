package com.example.pythonapp.service;

import com.example.pythonapp.model.*;

import java.util.List;
import java.util.Map;

public interface SolutionService {
    ShortSolution save(ShortSolution solution);
    LongSolution save(LongSolution solution);
    List<Solution> findStudentSolution(Student student);
    Solution findById(int id);
    LongSolution findLongSolutionById(int id);
    ShortSolution findShortSolutionById(int id);
    void updateSolution(int id, LongSolution solution);
    void updateSolution(LongSolution solution,String studentName);
    void delete(int id);
    List<Solution> getAllSolutionsByExercise(Exercise exercise);
    List<Map<String,String>> getAllTeacherExercises(Teacher teacher);
}
