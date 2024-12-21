package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
import io.restassured.RestAssured;
import com.example.pythonapp.model.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SolutionControllerTest {

    @Autowired
    UserController userController;
    @Autowired
    JWTToken token;

    @LocalServerPort
    private Integer port;
    private static String jwtToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @Order(1)
    void SetAuthorization() {


        Student student = new Student();
        student.setEmail("akaluza@student.agh.edu.pl");
        student.setName("alicja999");
        student.setPassword("password");
        JWTResponse jwt = null;
        try {
            jwt = userController.authenticate(student);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (jwt != null)
            jwtToken = jwt.getToken();
        Assertions.assertEquals(token.getUsernameFromToken(jwtToken),student.getName());
    }


    /**
     * get one's solutions with name exercise and score
     **/
    @Test
    @Order(2)
    public void getProgrammingSolutionsTest(){

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("/solution/programming")
                .then()
                .body("size()", equalTo(2))
                .statusCode(200);
    }


    @Test
    @Order(3)
    public void getLongSolutionTest(){

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("/solution/2")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    public void getLongPartsTest(){

       given()
               .when()
               .header("Authorization", "Bearer " + jwtToken)
               .get("/solution/parts/3")
               .then()
               .body("size()", equalTo(1))
               .statusCode(200);
    }

    @Test
    @Order(5)
    public void getShortSolutionTest(){

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("/solution/abc/102")
                .then()
                .body("size()", equalTo(1))
                .statusCode(200);
    }


}
