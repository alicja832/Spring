package com.example.pythonapp.service;
import com.example.pythonapp.dto.LongExerciseDto;
import com.example.pythonapp.model.*;
import javafx.util.Pair;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    Optional<LongExercise> findLongExerciseByName(String name);
    void update(int id,ShortExercise exercise);
    void update(int id,LongExercise exercise);
    void updateExercise(int id,Exercise exercise);
    LongExercise save(LongExercise longExercise);
    ShortExercise save(ShortExercise shortExercise);
    LongCorrectSolutionPart save(LongCorrectSolutionPart correctSolutionPart);
    TestingData save(TestingData testingData);
    void update(int id,TestingData testingData);
    List<TestingData> findAllTestingDataByExerciseId(int exerciseId);
    void update(int id,LongCorrectSolutionPart correctSolutionPart);
    List<LongCorrectSolutionPart> findAllLongCorrectSolutionByExerciseId(int exerciseId);
    List<LongCorrectSolutionPart> findAllLongCorrectSolutionByExerciseIdAndOrder(int exerciseId,int orderId);
    List<Exercise> findAllByTeacher_Id(int teacherId);
    void deleteLongCorrectSolutionPart(int exerciseId, int orderId);
    String runFunction(String function, String parameters);
    Pair<String, Integer> checkSolutionWithTests(LongSolution longSolution);
    int checkSolution(LongSolution longSolution);
    int checkSolution(ShortSolution shortSolution);
    Pair<LongExercise, ArrayList<LongCorrectSolutionPart>> createLongExerciseFromDto(LongExerciseDto exercise) throws Exception;
    LongExercise addLongExercise(LongExerciseDto exercise,Teacher teacher) throws Exception;
    void updateLongExercise(LongExerciseDto exercise) throws Exception;
}