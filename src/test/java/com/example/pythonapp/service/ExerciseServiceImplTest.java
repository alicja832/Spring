package com.example.pythonapp.service;
import com.example.pythonapp.model.*;
import com.example.pythonapp.dto.*;
import com.example.pythonapp.repository.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ExerciseServiceImplTest {

    private static LongExercise longExercise;
    private static ShortExercise shortExercise;
    private static TestingData testData;
    private static Teacher mockTeacher;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private LongExerciseRepository longExerciseRepository;

    @Mock
    private ShortExerciseRepository shortExerciseRepository;

    @Mock
    private TestingDataRepository testDataRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private ExerciseServiceImpl exerciseService;
    @InjectMocks
    private TeacherServiceImpl teacherService;


    @BeforeAll
    static void setup() {
        longExercise = new LongExercise("Test","Test","Test",10,"print(1)","def fun(a):\nreturn", true);
        shortExercise = new ShortExercise("TestShort","testA","testB",1,true,"testA","testB","testC","testD",'A');
        testData = new TestingData();
        String name = "alicja832";
        UserEntity userEntity = new UserEntity(name,"email@.gmail.com","password");
        mockTeacher = new Teacher(userEntity);
        mockTeacher.setId(1);
    }

    @Test
    @Order(1)
    void testSave() {

        LongExercise longExercise1 = longExercise;
        ShortExercise shortExercise1 = shortExercise;
        longExercise1.setId(1);
        shortExercise1.setId(2);
        when(teacherRepository.findByName("alicja832")).thenReturn(Optional.of(mockTeacher));
        longExercise.setTeacher(teacherService.findByName("alicja832").get());
        shortExercise.setTeacher(teacherService.findByName("alicja832").get());
        when(longExerciseRepository.save(longExercise)).thenReturn(longExercise1);
        longExercise = exerciseService.save(longExercise);
        when(shortExerciseRepository.save(shortExercise)).thenReturn(shortExercise1);
        shortExercise = exerciseService.save(shortExercise);
        testData.setExercise(longExercise);
        testData.setTestingData("1");
        when(testDataRepository.save(testData)).thenReturn(testData);
        testData = exerciseService.save(testData);
        Assertions.assertEquals("Test",longExercise.getName());
        Assertions.assertEquals("TestShort",shortExercise.getName());
        Assertions.assertEquals(2,shortExercise.getId());
        Assertions.assertEquals(1,longExercise.getId());
        Assertions.assertEquals("1",testData.getTestingData());

    }

    @Test
    @Order(2)
    void getOutTest() {

        LongExercise longExercise1 = longExercise;
        longExercise1.setName("updateTest");
        longExercise1.setSolutionSchema("updateTest");
        doAnswer((Answer<Void>) invocation -> {
                longExercise.setName(longExercise1.getName());
                return null;
        }).when(exerciseRepository).updateById(1,longExercise1.getName(),longExercise1.getIntroduction(),longExercise1.getContent(),longExercise1.getMaxPoints(),longExercise1.getAccess());
        exerciseService.update(1,longExercise1);
        Assertions.assertEquals("updateTest",longExercise.getName());

        ShortExercise shortExercise1 = shortExercise;
        shortExercise1.setName("updateTestAbc");
        doAnswer((Answer<Void>) invocation -> {
            shortExercise.setName(shortExercise1.getName());
            return null;
        }).when(exerciseRepository).updateById(2,shortExercise1.getName(),shortExercise1.getIntroduction(),shortExercise1.getContent(),shortExercise1.getMaxPoints(),shortExercise1.getAccess());
        exerciseService.update(2,shortExercise1);
        Assertions.assertEquals("updateTestAbc",shortExercise.getName());

    }
    @Test
    @Order(3)
    void findLongExerciseByNameTest() {
        when(longExerciseRepository.findByName("updateTest")).thenReturn(Optional.of(longExercise));
        Assertions.assertTrue(exerciseService.findLongExerciseByName("updateTest").isPresent());
    }

    @Test
    @Order(4)
    void findExerciseByIdTest() {
        when(exerciseRepository.findById(1)).thenReturn(Optional.of(longExercise));
        Assertions.assertTrue(exerciseService.findExerciseById(longExercise.getId()).isPresent());
    }

    @Test
    @Order(5)
    void findShortExerciseById() {
        when(shortExerciseRepository.findById(shortExercise.getId())).thenReturn(Optional.of(shortExercise));
        Assertions.assertTrue(exerciseService.findShortExerciseById(shortExercise.getId()).isPresent());
    }

    @Test
    @Order(6)
    void findLongExerciseByIdTest() {
        when(longExerciseRepository.findById(1)).thenReturn(Optional.of(longExercise));
        Assertions.assertTrue(exerciseService.findLongExerciseById(longExercise.getId()).isPresent());
    }

    @Test
    @Order(7)
    void findAllByTeacher_IdTest() {
        when(exerciseRepository.findAllByTeacher_Id(1)).thenReturn(List.of(shortExercise,longExercise));
        Assertions.assertEquals(2,exerciseService.findAllByTeacher_Id(1).size());
    }

    @Test
    @Order(8)
    void runFunctionTest() {
        Assertions.assertEquals("1\n",exerciseService.runFunction("def fun(a):\n\tprint(a)","1"));
    }

    @Test
    @Order(10)
    void deleteTest() {

        when(exerciseRepository.findById(longExercise.getId())).thenReturn(Optional.empty());
        Assertions.assertFalse(exerciseService.findExerciseById(longExercise.getId()).isPresent());
        when(exerciseRepository.findById(longExercise.getId())).thenReturn(Optional.empty());
        Assertions.assertFalse(exerciseService.findExerciseById(shortExercise.getId()).isPresent());

    }

    @Test
    @Order(11)
    void addLongExercise()
    {
        String [] words = {"def fun(a):\n\treturn a"};
        String [] tests = {"1"};
        Integer [] numbers = {1};
        LongExerciseDto longExercisedto;
        longExercisedto = new LongExerciseDto(3,true,"TestB","Test","Test",10,words,"def fun(a):\nreturn",tests,numbers);
        longExercise = new LongExercise("TestB","Test","Test",10,"print(1)","def fun(a):\nreturn", true);
        longExercise.setId(longExercisedto.getId());
        when(teacherRepository.findByName("alicja832")).thenReturn(Optional.of(mockTeacher));
        Teacher teacher = teacherService.findByName("alicja832").get();
        when(longExerciseRepository.save(longExercise)).thenReturn(longExercise);

        try {
           longExercise = exerciseService.addLongExercise(longExercisedto,teacher);
        }catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        Assertions.assertEquals("TestB",longExercise.getName());

    }

    
}