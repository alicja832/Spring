package com.example.pythonapp.service;
import com.example.pythonapp.model.*;
import com.example.pythonapp.dto.*;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExerciseServiceImplTest {

    @LocalServerPort
    private Integer port;
    private static LongExercise longExercise;
    private static ShortExercise shortExercise;
    private static TestingData testData;

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @BeforeAll
    static void setup() {
        longExercise = new LongExercise("Test","Test","Test",10,"print(1)","def fun(a):\nreturn", true);
        shortExercise = new ShortExercise("TestShort","testA","testB",1,true,"testA","testB","testC","testD",'A');
        testData = new TestingData();
    }

    @Test
    @Order(1)
    void testSave() {
        longExercise.setTeacher(teacherService.findByName("alicja832").get());
        shortExercise.setTeacher(teacherService.findByName("alicja832").get());
        longExercise = exerciseService.save(longExercise);
        shortExercise = exerciseService.save(shortExercise);
        testData.setExercise(longExercise);
        testData.setTestingData("1");
        testData = exerciseService.save(testData);
        Assertions.assertEquals("Test",longExercise.getName());
        Assertions.assertEquals("TestShort",shortExercise.getName());
    }

    @Test
    @Order(2)
    void updateExerciseTest() {
        longExercise.setName("updateTest");
        longExercise.setSolutionSchema("updateTest");
        shortExercise.setName("updateShortTest");
        exerciseService.update(longExercise.getId(),longExercise);
        exerciseService.update(shortExercise.getId(),shortExercise);
        longExercise = exerciseService.save(longExercise);
        Assertions.assertEquals("updateTest",longExercise.getName());
        Assertions.assertEquals("updateTest",longExercise.getSolutionSchema());
        Assertions.assertEquals("updateShortTest",exerciseService.save(shortExercise).getName());
    }

    @Test
    @Order(3)
    void findLongExerciseByNameTest() {

        Assertions.assertEquals(true,exerciseService.findLongExerciseByName("updateTest").isPresent());
    }


    @Test
    @Order(4)
    void getOutTest() {
        Assertions.assertEquals("1\n",exerciseService.getOut("print(1)"));
    }

    @Test
    @Order(5)
    void findExerciseByIdTest() {
        Assertions.assertEquals(true,exerciseService.findExerciseById(longExercise.getId()).isPresent());
    }

    @Test
    @Order(6)
    void findShortExerciseById() {
        Assertions.assertTrue(exerciseService.findShortExerciseById(shortExercise.getId()).isPresent());
    }

    @Test
    @Order(7)
    void findLongExerciseByIdTest() {
        Assertions.assertTrue(exerciseService.findLongExerciseById(longExercise.getId()).isPresent());
    }

    @Test
    @Order(8)
    void findAllByTeacher_IdTest() {
        Assertions.assertEquals(8,exerciseService.findAllByTeacher_Id(1).size());
    }

    @Test
    @Order(9)
    void runFunctionTest() {
        Assertions.assertEquals("1\n",exerciseService.runFunction("def fun(a):\n\tprint(a)","1"));
    }

    @Test
    @Order(10)
    void deleteTest() {

        exerciseService.delete(longExercise.getId());
        exerciseService.delete(shortExercise.getId());
        Assertions.assertEquals(false,exerciseService.findExerciseById(longExercise.getId()).isPresent());
        Assertions.assertEquals(false,exerciseService.findShortExerciseById(shortExercise.getId()).isPresent());
    }

    @Test
    @Order(11)
    void addLongExercise()
    {
        String [] words = {"def fun(a):\n\treturn a"};
        String [] tests = {"1"};
        Integer [] numbers = {1};
        LongExerciseDto longExercisedto;
        longExercisedto = new LongExerciseDto(0,true,"Test","Test","Test",10,words,"def fun(a):\nreturn",tests,numbers);
        try {
            exerciseService.addLongExercise(longExercisedto, teacherService.findByName("alicja832").get());
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        Assertions.assertEquals(true,exerciseService.findLongExerciseByName(longExercisedto.getName()).isPresent());
        if(exerciseService.findLongExerciseByName(longExercisedto.getName()).isPresent()) {
            exerciseService.delete(exerciseService.findLongExerciseByName(longExercisedto.getName()).get().getId());
            Assertions.assertEquals(false, exerciseService.findLongExerciseByName(longExercisedto.getName()).isPresent());
        }
    }
}