package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.controller.ExerciseController;
import com.example.pythonapp.details.AppUserDetails;
import com.example.pythonapp.dto.LongSolutionDto;
import com.example.pythonapp.dto.UserCreationDto;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.LongSolution;
import com.example.pythonapp.model.enums.Role;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
//DOROBIĆ TESTY
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExerciseControllerTest {

    @Autowired
    ExerciseController exerciseController;
    @Autowired
    UserController userController;
    @Autowired
    JWTToken token;
    @LocalServerPort
    private Integer port;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }





//        @Test
//        @Order(1)
//        void TestAddSolution() {
//        Exercise exercise = exerciseController.listExercises().get(0).getKey();
//        Solution solution = new Solution();
//        solution.setExercise(exercise);
//        solution.set
   // }


//
//    @Test
//    @Order(3)
//    void UnAuthorized() {
//
//
//        given()
//                .when()
//                .get("user/student/someone@gmail.com")
//                .then()
//                .statusCode(403);
//
//    }
//


}