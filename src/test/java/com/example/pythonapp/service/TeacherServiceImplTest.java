package com.example.pythonapp.service;

import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeacherServiceImplTest {

    @Autowired
    private TeacherService teacherService;
    private static Teacher teacher;

    @BeforeAll
    static void setup() {

        teacher = new Teacher();
        teacher.setName("John");
        teacher.setEmail("john@gmail.com");
        teacher.setPassword("tttttt");

    }

    @Test
    @Order(1)
    void save() {

        teacher = teacherService.save(teacher);
        teacherService.save(teacher);

    }

    @Test
    @Order(2)
    void findByName() {
        Assertions.assertTrue(teacherService.findByName("John").isPresent());
    }

    @Test
    @Order(3)
    void delete() {
        teacherService.delete(teacher.getId());
        Assertions.assertFalse(teacherService.findByName("John").isPresent());
    }

}