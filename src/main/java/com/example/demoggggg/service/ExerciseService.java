package com.example.demoggggg.service;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.UserEntity;

import java.util.List;

public interface ExerciseService {

    Exercise save(Exercise exercise);
    Solution save(Solution solution);
    void delete(long id);
    List<Exercise> getAllExercises();
    String getOut(String text);
    Exercise findExerciseById(long id);
    void update(long id,Exercise exercise);
    List<Solution> findStudentSolution(Student student);
    Solution findSolutionById(int id);
    void updateSolution(int id,Solution solution);
    long deleteSolutionById(long id);

}