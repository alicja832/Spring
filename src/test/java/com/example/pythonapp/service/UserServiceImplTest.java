package com.example.pythonapp.service;

import com.example.pythonapp.dto.VerificationRequest;
import com.example.pythonapp.model.Teacher;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @Test
    @Order(1)
    void findByNameTest()
    {
        Assertions.assertTrue(userService.findByName("alicja999").isPresent());
    }

    @Test
    @Order(2)
    void findByEmailTest()
    {
        Assertions.assertTrue(userService.findByEmail("akaluza@student.agh.edu.pl").isPresent());
    }


}