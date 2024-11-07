package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.details.AppUserDetails;
import com.example.pythonapp.jwt.JWTResponse;
import com.example.pythonapp.jwt.JwtToken;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.service.UserService;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {

    @Autowired
    UserController userController;
    @Autowired
    JwtToken token;
    @LocalServerPort
    private Integer port;
    private String jwtToken;
    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
    @Test
    void Authorized() {
        Teacher student = new Teacher();
        student.setEmail("someone@gmail.com");
        student.setName("someone");
        student.setRole("TEACHER");
        student.setPassword("password");
        userController.add(student).getBody();
        student.setPassword("password");
        JWTResponse jwt=null;
        try {
            jwt = userController.authenticate(student);
        }catch (Exception exception)
        {
            exception.printStackTrace();
        }

        if(jwt!=null)
            jwtToken = jwt.getjwtToken();
        AppUserDetails app = new AppUserDetails(student);
        Assertions.assertEquals(token.getUsernameFromToken(jwtToken),student.getName());
        Assertions.assertEquals(token.validateToken(jwtToken,app),true);



            given()
                    .when()
                    .header("Authorization", "Bearer " + jwtToken)
                    .get("user/")
                    .then()
                    .statusCode(200)
                    .body("data.size()", equalTo(1));

    }
    @Test
    void UnAuthorized() {


        given()
                .when()
                .get("user/student/someone@gmail.com")
                .then()
                .statusCode(403);

    }
    @Test
    void AuthorizedPost() {

        given()
                .when()
                .post("user/")
                .then()
                .statusCode(403);

    }
   
//    @Test
//    void authorizedPerson()
//    {
//        /*  @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    @Column(unique = true, name = "id",nullable = false)
////    private int id;
////    @Column(unique = true, name="name",updatable=true)
////    private String name;
////    @Column(unique = true, name="introduction",updatable=true)
////    private String introduction;
////    @Column(unique = true, name="content",updatable=true)
////    private String content;
////    @Column(name="maxpoints", updatable=true)
////    private int maxPoints;
////    @Column(name="correctsolution", updatable=true)
////    private String correctSolution;
////    @Column(name="correctoutput", updatable=true)
////    private String correctOutput = null;
//   // teacher
//
//        */
//
//        Teacher student = new Teacher();
//        student.setEmail("teacher@gmail.com");
//        student.setName("someonew");
//        student.setPassword("password");
//        userController.add(student).getBody();
//        student.setPassword("password");
//
//        try {
//            jwtToken = userController.authenticate(student).getjwtToken();
//        }
//        catch (Exception exception)
//        {
//            exception.printStackTrace();
//        }
//        String anotherjsonBody = "{\"name\":\"someonew\",\"email\":\"teacher@gmail.com\",\"password\":\"password\"}";
//        String jsonBody = "{\"name\":\"dhadiprasetyo@gmail.com\",\"introduction\":\"Jzr0sOORprar10kay6CweZ5FNYP2\",\"content\":\"dddddd\",\"correctSolution\":\"costam\",\"teacher\":\""+anotherjsonBody+"\"}";
//         given()
//                .body(jsonBody)
//                .when()
//                  .header("Content-Type", "application/json")
//                 .header("Authorization", "Bearer " + jwtToken)
//                 .post("/exercise/")
//                 .then().assertThat().statusCode(200);
//    }

}   
