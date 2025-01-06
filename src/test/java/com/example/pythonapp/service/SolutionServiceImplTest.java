package com.example.pythonapp.service;
import com.example.pythonapp.model.*;
import com.example.pythonapp.repository.ExerciseRepository;
import com.example.pythonapp.repository.ShortSolutionRepository;
import com.example.pythonapp.repository.LongSolutionRepository;
import com.example.pythonapp.repository.ShortSolutionRepository;
import com.example.pythonapp.repository.SolutionRepository;
import com.example.pythonapp.repository.StudentRepository;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SolutionServiceImplTest {

    private static LongSolution longSolution;
    private static ShortSolution shortSolution;
    private static Student mockStudent;
    private static Exercise exercise;
    @Mock
    private SolutionRepository solutionRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock
    private LongSolutionRepository longSolutionRepository;
    @Mock
    private ShortSolutionRepository shortSolutionRepository;

    @InjectMocks
    private SolutionService solutionService;
    @InjectMocks
    private StudentService studentService;
    @InjectMocks
    private ExerciseService exerciseService;



    @BeforeAll
    static void setup() {

        longSolution = new LongSolution();
        shortSolution = new ShortSolution();
        longSolution.setSolutionContent("def fun(a):\n\tprint(2)");
        shortSolution.setAnswer('A');

        String name = "alicja832";
        UserEntity userEntity = new UserEntity(name,"email@.gmail.com","password");
        mockStudent = new Student(userEntity);
        mockStudent.setId(1);

        exercise = new Exercise();
    }

    @Test
    @Order(1)
    void testSave() {

        longSolution.setExercise(exercise);
        longSolution.setStudent(mockStudent);
        shortSolution.setStudent(mockStudent);
        solutionService.save(longSolution);


        LongSolution longSolution1 = longSolution;
        ShortSolution shortSolution1 = shortSolution;
        longSolution1.setId(1);
        shortSolution1.setId(2);
        when(studentRepository.findByName("alicja832")).thenReturn(Optional.of(mockStudent));

        when(longSolutionRepository.save(longSolution)).thenReturn(longSolution1);
        longSolution = solutionService.save(longSolution);
        when(shortSolutionRepository.save(shortSolution)).thenReturn(shortSolution1);
        shortSolution = solutionService.save(shortSolution);

        Assertions.assertEquals("def fun(a):\n\tprint(2)",longSolution.getSolutionContent());
        Assertions.assertEquals('A',shortSolution.getAnswer());
        Assertions.assertEquals(2,shortSolution.getId());
        Assertions.assertEquals(1,longSolution.getId());
    }



    @Test
    @Order(2)
    void findStudentSolutionTest() {

        when(solutionRepository.findAllByStudentEquals(mockStudent)).thenReturn(List.of(longSolution,shortSolution));
        Assertions.assertEquals(2,solutionService.findStudentSolution(mockStudent).size());
    }

    @Test
    @Order(3)
    void findByIdTest() {

        when(solutionRepository.findById(longSolution.getId())).thenReturn(Optional.of(longSolution));
        Assertions.assertEquals(solutionService.findById(longSolution.getId()).getId(),longSolution.getId());

        when(solutionRepository.findById(shortSolution.getId())).thenReturn(Optional.of(shortSolution));
        Assertions.assertEquals(solutionService.findById(shortSolution.getId()).getId(),shortSolution.getId());
    }


    @Test
    @Order(4)
    void findLongSolutionByIdTest() {
        when(solutionRepository.findById(longSolution.getId())).thenReturn(Optional.of(longSolution));
        Assertions.assertEquals(longSolution.getId(),solutionService.findLongSolutionById(longSolution.getId()).getId());
    }


    @Test
    @Order(5)
    void findShortSolutionByIdTest() {
        when(solutionRepository.findById(longSolution.getId())).thenReturn(Optional.of(longSolution));
        Assertions.assertEquals(longSolution.getId(),solutionService.findLongSolutionById(longSolution.getId()).getId());
    }

    @Test
    @Order(6)
    void updateSolutionTest() {
        longSolution.setSolutionContent("def fun(a):\n\tprint(12)");
        Assertions.assertEquals("def fun(a):\n\tprint(12)",solutionService.findById(longSolution.getId()));

    }

    @Test
    @Order(7)
    void getAllSolutionByExercise() {

        when(solutionRepository.getAllByExercise(exercise)).thenReturn(List.of(longSolution,shortSolution));
        Assertions.assertFalse(solutionService.getAllSolutionsByExercise(exercise).isEmpty());
    }


    @Test
    @Order(8)
    void deleteTest() {

        when(solutionRepository.findById(longSolution.getId())).thenReturn(Optional.empty());
        exerciseService.delete(longSolution.getId());
        Assertions.assertThrows(RuntimeException.class, solutionService.findById(longSolution.getId()));

        when(solutionRepository.findById(shortSolution.getId())).thenReturn(Optional.empty());
        exerciseService.delete(shortSolution.getId());
        Assertions.assertThrows(solutionService.findById(shortSolution.getId()));

    }
}