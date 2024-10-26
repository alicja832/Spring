package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.details.AppUserDetails;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JwtToken;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest()
public class UserControllerTest {

    @Autowired
    UserController userController;
    @Autowired
    JwtToken token;
    @Test
    void Authorized() {
        Student student = new Student();
        student.setEmail("someone@gmail.com");
        student.setName("someone");
        student.setPassword("password");
        userController.add(student).getBody();
        student.setPassword("password");
        JWTResponse  jwt=null;
        try {
            jwt = userController.authenticate(student);
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }
        String jwtToken=null;
        if(jwt!=null)
            jwtToken = jwt.getjwtToken();
        AppUserDetails app = new AppUserDetails(student);
        Assertions.assertEquals(token.getUsernameFromToken(jwtToken),student.getName());
        Assertions.assertEquals(token.validateToken(jwtToken,app),true);


//
//            given()
//                    .when()
//                    .header("Authorization", "Bearer " + jwtToken)
//                    .get("/student/someone")
//                    .then()
//                    .statusCode(200)
//                    .body("data.size()", equalTo(1));

    }   

    
 
}   
