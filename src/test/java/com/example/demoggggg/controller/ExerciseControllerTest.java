package com.example.demoggggg.controller;

import com.example.demoggggg.model.Exercise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO test jest nieskończony
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ExerciseControllerTest {

    @Autowired
    private ExerciseController exerciseController;


    @Test
    void test_getresponse()
    {
        String code = "\ndef fun(a):\n\tprint(a)\nfun(1)\n";
        String getresponse = exerciseController.getresponse(code);
        assertEquals("1\n", getresponse);
    }
    @Test
    void test_update()
    {
        Exercise exercise = new Exercise();
        exercise.setName("a");
        exercise.setIntroduction("a");
        exercise.setCorrectSolution("a");
        exercise.setMaxPoints(1);
        exercise.setContent("a");
        exerciseController.add(exercise);
        exercise.setName("b");
        exerciseController.update(exercise);
        exercise = exerciseController.FindById(1).get(0);
        assertEquals("b",exercise.getName());
    }
}