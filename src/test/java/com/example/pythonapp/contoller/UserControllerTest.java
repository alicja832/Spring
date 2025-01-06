package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.details.AppUserDetails;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * This class test only endpoints, which not demand the database connection
 * Those which demand this connection are in folder service
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserControllerTest {

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

        Teacher teacher = new Teacher();
        teacher.setEmail("alicja.zosia.k@gmail.com");
        teacher.setName("alicja832");
        teacher.setPassword(("password"));
        JWTResponse jwt = null;
        try {
            jwt = userController.authenticate(teacher);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        if (jwt != null)
            jwtToken = jwt.getToken();
        Assertions.assertEquals(token.getUsernameFromToken(jwtToken), teacher.getName());
    }

    @Test
    @Order(3)
    void TestGetUserInformation() {

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("/user/")
                .then()
                .statusCode(200);
    }

    @Test
    @Order(4)
    void TestGetStudentRanking() {

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("/user/ranking")
                .then()
                .body("size()", equalTo(2))
                .statusCode(200);
    }

    @Test
    @Order(5)
    void TestGetTeacherExercises() {

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("/user/exercises")
                .then()
                .body("size()", equalTo(5))
                .statusCode(200);
    }

    @Test
    @Order(6)
    void TestGetStudentPosition() {

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

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("/user/position")
                .then()
                .statusCode(200);
    }
 
}