package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
import com.example.pythonapp.model.Teacher;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExerciseControllerTest {

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
     @Order(2)
     void TestGetAbcExercises() {

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("exercise/abc")
                .then()
                .body("size()", equalTo(2))
                .statusCode(200);
   
    }


    @Test
    @Order(3)
    void TestGetAbcExercisesUnAuthorized() {

        given()
                .when()
                .get("exercise/abc")
                .then()
                .body("size()", equalTo(1))
                .statusCode(200);

    }

    @Test
    @Order(4)
    void TestGetProgrammingExercises() {

        given()
                .when()
                .header("Authorization", "Bearer " + jwtToken)
                .get("exercise/programming")
                .then()
                .body("size()", equalTo(2))
                .statusCode(200);

    }

    @Test
    @Order(5)
    void  TestGetProgrammingExercisesUnAuthorized() {

        given()
                .when()
                .get("exercise/programming")
                .then()
                .body("size()", equalTo(1))
                .statusCode(200);

    }

    @Test
    @Order(6)
    public void TestFindLongExerciseById() {

        given()
                .when()
                .get("/exercise/one/programming/1")
                .then()
                .body("size()", equalTo(1))
                .statusCode(200);


        }

    @Test
    @Order(7)
    public void TestFindShortExerciseById() {
        given()
                .when()
                .get("/exercise/one/abc/2")
                .then()
                .body("size()", equalTo(1))
                .statusCode(200);

    }


}