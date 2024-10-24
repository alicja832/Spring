package com.example.pythonapp.service;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.UserEntity;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
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
    Exercise findExerciseByName(String name);

}