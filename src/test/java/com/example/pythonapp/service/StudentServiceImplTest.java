package com.example.pythonapp.service;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.repository.StudentRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentServiceImplTest {

    @Mock
    StudentRepository studentRepository;
    @InjectMocks
    StudentServiceImpl studentService;
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

        Student student1 = student;
        student1.setId(1);
        when(studentRepository.save(student)).thenReturn(student1);
        student = studentService.save(student);
        Assertions.assertEquals(1,student.getId());

    }

    @Test
    @Order(2)
    void update() {

        Student student1 = student;
        student1.setScore(12);
        doAnswer((Answer<Void>) invocation -> {
            student.setScore(student1.getScore());
            return null;
        }).when(studentRepository).updateById(student1.getId(),student1.getScore());
        studentService.update(student1.getId(),student1.getScore());
        Assertions.assertEquals(12,student.getScore());
    }

    @Test
    @Order(3)
    void listStudents()
    {
        when(studentRepository.findAll()).thenReturn(List.of(student));
        Assertions.assertFalse(studentService.listStudents().isEmpty());
    }

    @Test
    @Order(4)
    void findByName() {

        when(studentRepository.findByName("John")).thenReturn(Optional.of(student));
        Assertions.assertTrue(studentService.findByName("John").isPresent());
    }


}