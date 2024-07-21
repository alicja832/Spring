package com.example.demoggggg.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TeacherTest {
    Teacher teacher;
    @Test
    void deleteexc() {
        teacher = new Teacher();
        Exercise exercise = new Exercise();
        teacher.AddExc(exercise);
        teacher.getExercises().remove(exercise);
        assertTrue(teacher.getExercises().isEmpty());

    }
}