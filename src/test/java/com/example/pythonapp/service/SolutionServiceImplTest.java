package com.example.pythonapp.service;
import com.example.pythonapp.model.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SolutionServiceImplTest {

    @LocalServerPort
    private Integer port;
    private static LongSolution longSolution;
    private static ShortSolution shortSolution;


    @Autowired
    private SolutionService solutionService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ExerciseService exerciseService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeAll
    static void setup() {

        longSolution = new LongSolution();
        shortSolution = new ShortSolution();
        longSolution.setSolutionContent("def fun(a):\n\tprint(2)");
        shortSolution.setAnswer('A');
    }

    @Test
    @Order(1)
    void testSave() {
        longSolution.setExercise(exerciseService.findExerciseById(3).get());
        longSolution.setStudent(studentService.findByName("alicja999").get());
        shortSolution.setStudent(studentService.findByName("alicja999").get());
        solutionService.save(longSolution);
    }



    @Test
    @Order(2)
    void findStudentSolutionTest() {

        solutionService.findStudentSolution(studentService.findByName("alicja999").get());
    }

    @Test
    @Order(3)
    void findByIdTest() {

        Assertions.assertEquals(1, solutionService.findById(longSolution.getId()));
    }


    @Test
    @Order(4)
    void findLongSolutionByIdTest() {
        Assertions.assertEquals(1, solutionService.findLongSolutionById(longSolution.getId()));
    }


    @Test
    @Order(5)
    void findShortSolutionByIdTest() {
       Assertions.assertEquals(1,solutionService.findShortSolutionById(shortSolution.getId()));
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
        Assertions.assertFalse(solutionService.getAllSolutionsByExercise(exerciseService.findExerciseById(3).get()).isEmpty());
    }


    @Test
    @Order(8)
    void deleteTest() {

        exerciseService.delete(longSolution.getId());
        exerciseService.delete(shortSolution.getId());
        Assertions.assertFalse(exerciseService.findExerciseById(longSolution.getId()).isPresent());
        Assertions.assertFalse(exerciseService.findShortExerciseById(shortSolution.getId()).isPresent());
    }
}