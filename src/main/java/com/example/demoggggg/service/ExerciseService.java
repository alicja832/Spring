package com.example.demoggggg.service;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;

import java.util.List;

public interface ExerciseService {

    Exercise save(Exercise exercise);
    void delete(int id);
    List<Exercise> getAllExercises();
    String getOut(String text);
    Exercise findById(int id);
    void update(int id,Exercise exercise);
    List<Solution> findStudentSolution(Student student);
    Solution findByI(int id);
    void updateSolution(int id,Solution solution);
}
