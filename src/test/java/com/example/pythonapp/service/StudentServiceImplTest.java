package com.example.pythonapp.service;

import com.example.pythonapp.model.Student;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceImplTest {
    @Autowired
    private StudentService studentService;
    private static Student student;
    @BeforeAll
    static void setup() {

        student = new Student();
        student.setName("John");
        student.setEmail("john@gmail.com");
        student.setPassword("password");

    }
    @Test
    @Order(1)
    void save() {

        student = studentService.save(student);
        studentService.save(student);

    }

    @Test
    @Order(2)
    void update() {
        studentService.update(student.getId(),10);
        Assertions.assertEquals(10,studentService.findByName(student.getName()).get().getScore());
    }

    @Test
    @Order(3)
    void listStudents() {
        Assertions.assertFalse(studentService.listStudents().isEmpty());
    }

    @Test
    @Order(4)
    void findByName() {
        Assertions.assertTrue(studentService.findByName("John").isPresent());
    }

    @Test
    @Order(5)
    void delete() {
        studentService.delete(student.getId());
        Assertions.assertFalse(studentService.findByName("John").isPresent());
    }
}