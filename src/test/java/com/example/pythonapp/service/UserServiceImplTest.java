package com.example.pythonapp.service;
import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    private static UserEntity user;

    @BeforeAll
    static void setup() {

        user = new UserEntity();
        user.setName("John");
        user.setEmail("john@gmail.com");
        user.setPassword("tttttt");

    }

    @Test
    @Order(1)
    void findByNameTest()
    {
        when(userRepository.findByName("John")).thenReturn(Optional.of(user));
        Assertions.assertTrue(userService.findByName("John").isPresent());
    }

    @Test
    @Order(2)
    void findByEmailTest()
    {
        when(userRepository.findByEmail("john@gmail.com")).thenReturn(Optional.of(user));
        Assertions.assertTrue(userService.findByEmail("john@gmail.com").isPresent());
    }


}