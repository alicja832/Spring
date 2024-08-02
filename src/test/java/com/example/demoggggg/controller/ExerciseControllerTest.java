package com.example.demoggggg.controller;

import com.example.demoggggg.dto.ExerciseDto;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.ScheduledTaskObservationDocumentation;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;

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

    static ExerciseDto exercise;
    static Exercise exc;
    static Student student;
    static Teacher teacher;
    static Solution solution;
    @BeforeAll
    static void setUp()
    {
        teacher=new Teacher();
        teacher.setName("a");
        teacher.setEmail("email");
        teacher.setPassword("emaifl");

        exercise = new ExerciseDto();
        exercise.setName("a");
        exercise.setIntroduction("a");
        exercise.setCorrectSolution("\nprint('a')\n");
        exercise.setMaxPoints(1);
        exercise.setContent("a");



        student=new Student();
        student.setName("asss");
        student.setEmail("emails");
        student.setPassword("emaifl");

        solution = new Solution();
        solution.setSolutionContent("\nprint('a')\n");


    }
    @BeforeEach
    void before()
    {
        userController.add(teacher);
        userController.add(student);
        exercise.setTeacher(teacher);
        solution.setStudent(student);
        exc = exerciseController.add(exercise).getBody();
        solution.setExercise(exc);
        solution = exerciseController.addSolution(solution).getBody();
    }
    @AfterEach
    void after()
    {
//        exerciseController.delete(1);
//        exerciseController.deleteSolution(1);
//        userController.delete(1);
//        userController.delete(2);


    }
    /**
     * it doesn't work I don't know why
     */
    @Test
    void test_getresponse()
    {
        String code = "\ndef fun(a):\n\tprint(a)\nfun(1)\n";
        String getresponse = exerciseController.getresponse(code);
        assertEquals("1\n", getresponse);
    }

    /**
     * dziala
     */
    @Test
    void test_update_exercise()
    {
        exc.setName("b");
        exc.setIntroduction("b");
        exerciseController.update(exc);
        exc = exerciseController.FindById(1).get(0);
        assertEquals("b",exc.getName());
        assertEquals("b",exc.getIntroduction());
    }

    /**
     *
     */
    @Test
    void test_update_solution()
    {
        solution.setSolutionContent("b");
        exerciseController.updateSolution(solution);
        solution = exerciseController.getSolution(1).get(0);
        assertEquals("b",solution.getSolutionContent());
        assertEquals("b",exerciseController.getSolution(1).get(0).getSolutionContent());
    }
    @Test
    void test_check()
    {

        int score = exerciseController.checkSolution(solution);
        assertEquals(1,score);


    }
    @Test
    void get_solutions()
    {
        List<Map<String,String>> list= exerciseController.getSolutions("emails");
        exerciseController.checkSolution(solution);
        exerciseController.updateSolution(solution);
        assertEquals("1",list.get(0).get("score"));
        assertEquals("a",list.get(0).get("name"));

    }
    /**
     * dziala
     */
    @Test
    void test_solution()
    {
//        assertEquals(1,student.getId());
//        assertEquals(1,userController.getStudent("asss").getId());
        assertEquals("\nprint('a')\n",student.getSolutions().get(0).getSolutionContent());
    }
}