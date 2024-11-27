package com.example.pythonapp.service;
import com.example.pythonapp.model.*;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface ExerciseService {

    void delete(int id);
    List<ShortExercise> getAllShortExercises();
    List<LongExercise> getAllLongExercises();
    String getOut(String text);
    Optional<ShortExercise> findShortExerciseById(int id);
    Optional<LongExercise> findLongExerciseById(int id);
    Optional<Exercise> findExerciseById(int id);
    Optional<ShortExercise> findShortExerciseByName(String name);
    Optional<LongExercise> findLongExerciseByName(String name);
    void update(int id,ShortExercise exercise);
    void update(int id,LongExercise exercise);
    void updateExercise(int id,Exercise exercise);
    Optional<Exercise> findExerciseByName(String name);
    LongExercise save(LongExercise longExercise);
    ShortExercise save(ShortExercise shortExercise);
    LongCorrectSolutionPart save(LongCorrectSolutionPart correctSolutionPart);
    void update(int id,LongCorrectSolutionPart correctSolutionPart);
    List<LongCorrectSolutionPart> findAllLongCorrectSolutionByExerciseId(int exerciseId);
    List<Exercise> findAllByTeacher_Id(int teacherId);
}