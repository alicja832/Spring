package com.example.pythonapp.service;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.UserEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface ExerciseService {

    Exercise save(Exercise exercise);
    void delete(long id);
    List<Exercise> getAllExercises();
    String getOut(String text);
    Optional<Exercise> findExerciseById(long id);
    void update(long id,Exercise exercise);
    Optional<Exercise> findExerciseByName(String name);

}