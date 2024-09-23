package com.example.demoggggg.controller;

import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.service.ExerciseService;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.ScheduledTaskObservationDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.StringWriter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TODO test jest nieskończony
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class ExerciseControllerTest {

    @Autowired
    private ExerciseController exerciseController;
    @Autowired
    private UserController userController;


    @Test
    void test_getresponse()
    {
        String code = "\nclass Car:\n\tdef fun(self):\n\t\tprint('1')\nCar().fun()\n";
        PythonInterpreter pythonInterpreter = new PythonInterpreter();
        StringWriter writer = new StringWriter();
        pythonInterpreter.setOut(writer);
        pythonInterpreter.exec( code);
       // String getresponse = exerciseController.getresponse(code);
        assertEquals("1\n", writer.toString());
    }
    @Test
    void test_update_exercise()
    {
        Exercise exercise = new Exercise();
        exercise.setName("a");
        exercise.setIntroduction("a");
        exercise.setCorrectSolution("print('a')");
        exercise.setMaxPoints(1);
        exercise.setContent("a");
//        exercise = exerciseController.add(exercise);
        exercise.setName("b");
        exercise.setIntroduction("b");
        exerciseController.update(exercise);
        exercise = exerciseController.FindById(1).get(0);

        assertEquals("b",exercise.getName());
        assertEquals("b",exercise.getIntroduction());
    }
    @Test
    void test_setExercises()
    {

        List<Pair<Exercise,Boolean>> list = exerciseController.listExercises();
        assertEquals("[-1, -1, 0, 4, 5, 6, 10, 14]\n",list.get(0).getKey().getCorrectOutput());

    }
    @Test
    void test_update_solution()
    {
        Student student=new Student();
        student.setName("a");
        userController.add(student);

        Exercise exercise = new Exercise();
        exercise.setName("a");
        exercise.setIntroduction("a");
        exercise.setCorrectSolution("print('a')");
        exercise.setMaxPoints(1);
        exercise.setContent("a");
//        exercise = exerciseController.add(exercise);

        Solution solution = new Solution();
        solution.setSolutionContent("a");
        solution.setExercise(exercise);
//        solution = userController.addSolution(solution);

        solution.setSolutionContent("b");
        exerciseController.updateSolution(solution);
        solution = exerciseController.getSolution(solution.getId()).get(0);
        assertEquals("b",solution.getSolutionContent());
        assertEquals("b",userController.getStudent().get(0).getSolutions().get(0).getSolutionContent());
    }
    @Test
    void test_check()
    {
        Student student=new Student();
        student.setName("a");
        userController.add(student);

        Exercise exercise = new Exercise();
        exercise.setName("a");
        exercise.setIntroduction("a");
        exercise.setCorrectSolution("print('a')");
        exercise.setMaxPoints(1);
        exercise.setContent("a");
//        exercise = exerciseController.add(exercise);

        Solution solution = new Solution();
        solution.setSolutionContent("a");
        solution.setExercise(exercise);
        solution.setOutput("a");

        int score = exerciseController.checkSolution(solution);
        assertEquals(1,score);


    }
}