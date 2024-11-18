package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.controller.ExerciseController;
import com.example.pythonapp.details.AppUserDetails;
import com.example.pythonapp.dto.LongSolutionDto;
import com.example.pythonapp.dto.UserCreationDto;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExerciseControllerTest {

    @Autowired
    ExerciseController exerciseController;
    @Autowired
    JWTToken token;
    @LocalServerPort
    private Integer port;
    private String jwtToken;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

   @Test
   @Order(1)
   void Authorized() {
       
       Exercise exercise = exerciseController.FindLongExerciseById(1).get(0);
       LongSolutionDto solution = new LongSolutionDto();
       solution.setSolutionContent("print(1)");
       solution.setOutput("1");
       solution.setExercise(exercise);
       Assertions.assertEquals(0,exerciseController.checkSolution(solution));
   }
//
//    @Test
//    @Order(2)
//    void ShouldGiveToken() {
//        Teacher teacher = new Teacher();
//        teacher.setEmail("someone@gmail.com");
//        teacher.setName("someone");
//        teacher.setPassword("password");
//        userController.add(new UserCreationDto(teacher.getName(),teacher.getEmail(),teacher.getPassword(), Role.TEACHER)).getBody();
//        teacher.setPassword("password");
//        JWTResponse jwt = null;
//        try {
//            jwt = userController.authenticate(teacher);
//        } catch (Exception exception) {
//            exception.printStackTrace();
//        }
//
//        if (jwt != null)
//            jwtToken = jwt.getJwtToken();
//
//    }
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
   @Test
   @Order(2)
   void AuthorizedPost() {

       given()
               .when()
               .post("user/")
               .then()
               .statusCode(403);

   }

}