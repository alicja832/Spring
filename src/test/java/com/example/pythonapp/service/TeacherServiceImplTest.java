package com.example.pythonapp.service;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.repository.TeacherRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.util.Optional;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TeacherServiceImplTest {

    @Mock
    private TeacherRepository teacherRepository;
    @InjectMocks
    private TeacherServiceImpl teacherService;

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

        Teacher teacher1 = teacher;
        teacher.setId(1);
        when(teacherRepository.save(teacher)).thenReturn(teacher1);
        teacher = teacherService.save(teacher);
        Assertions.assertEquals(1,teacher.getId());

    }

    @Test
    @Order(2)
    void findByName() {
        when(teacherRepository.findByName("John")).thenReturn(Optional.of(teacher));
        Assertions.assertTrue(teacherService.findByName("John").isPresent());
    }



}