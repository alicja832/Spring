package com.example.demoggggg.controller;

import com.example.demoggggg.dto.ExerciseDto;
import com.example.demoggggg.model.Teacher;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.UserEntity;
import com.example.demoggggg.model.enums.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
/**
 * TODO test jest nieskończony
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController userController;
    @Autowired
    private ExerciseController exerciseController;
    private Teacher teacher;
    @Test
    void register()
    {
        UserEntity user=new UserEntity();
        user.setEmail("a");
        user.setPassword("a");
        user.setName("a");
        assertEquals(userController.login(user), "TEACHER");
    }
    @BeforeEach
    void before()
    {
        teacher = new Teacher();
        teacher.setPassword("a");
        teacher.setName("a");
        teacher.setEmail("a");
        teacher.setRole("teacher");

         teacher = userController.add(teacher).getBody();
    }
    @Test
    void addSth()
    {

        ExerciseDto exercise = new ExerciseDto();
        exercise.setName("a");
        exercise.setIntroduction("a");
        exercise.setCorrectSolution("\nprint('a')\n");
        exercise.setMaxPoints(1);
        exercise.setContent("a");
        exercise.setTeacher(teacher);
        Exercise exc = exerciseController.add(exercise).getBody();

       assertEquals("a",userController.getExercises("a").get(0).getName());
       assertEquals("a",teacher.getExercises().get(0).getName());
    }
    @Test
    void addtakeBY() {
        teacher = userController.get("a");
        assertTrue(teacher.getExercises().isEmpty());
    }

}