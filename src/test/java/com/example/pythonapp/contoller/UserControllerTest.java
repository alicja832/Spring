package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.details.AppUserDetails;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JWTToken;
import com.example.pythonapp.model.Teacher;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

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

//    @Test
//    @Order(1)
//    void TestAuthorization() {
//        Teacher teacher = new Teacher();
//        teacher.setEmail("alicja.zosia.k@gmail.com");
//        teacher.setName("alicja832");
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
//        AppUserDetails app = new AppUserDetails(teacher);
//        Assertions.assertEquals(token.getUsernameFromToken(jwtToken), teacher.getName());
//        Assertions.assertEquals(token.validateToken(jwtToken, app), true);
//
//        given()
//                .when()
//                .header("Authorization", "Bearer " + jwtToken)
//                .get("user/")
//                .then()
//                .statusCode(200);
//    }
    @Test
    @Order(2)
    void TestUnAuthorized() {


        given()
                .when()
                .get("user/student/someone@gmail.com")
                .then()
                .statusCode(403);

    }
}