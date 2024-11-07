package com.example.pythonapp.contoller;
import com.example.pythonapp.controller.ExerciseController;
import com.example.pythonapp.controller.UserController;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.service.ExerciseService;
import javafx.util.Pair;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;
import org.springframework.boot.test.web.server.LocalServerPort;
import java.util.ArrayList;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExerciseTest {
    
    @Autowired
    ExerciseController exercisecontroller;
    @Autowired
    UserController userController;
    String jwtToken;
    @LocalServerPort
    
   
    private Integer port;
    private ExerciseService exerciseService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }
    
    @Test
    @Order(1)    
    void pythonInterpeterTest() {

        Assertions.assertEquals( "a\n",exercisecontroller.getresponse("print('a')"));
        Assertions.assertEquals( "[-1, -1, 0, 4, 5, 6, 10, 14]\n",exercisecontroller.getresponse("A = [-1,0,10,-1,14,4,6,5]\nn=len(A)\nfor i in range(n):\n\tmin=i\n\tfor j in range(i,n):\n\t\tif(A[j]<A[min]):\n\t\t\tmin = j\n\ttmp=A[i]\n\tA[i]=A[min]\n\tA[min]=tmp\nprint(A)"));
        Assertions.assertEquals( 1,exercisecontroller.listExercises().size());
        Assertions.assertEquals( "Traceback (most recent call last):\n  File \"<string>\", line 1, in <module>\nNameError: name 'a' is not defined\n\n\t",exercisecontroller.getresponse("a"));
        Assertions.assertEquals( "a\n",exercisecontroller.getresponse("print('a')"));
    }   
    @Test
    @Order(3)
    void TestdeleteExercise(){
   	    

      	Teacher student = new Teacher();
        student.setEmail("teacherz@gmail.com");
        student.setName("someonew");
        student.setPassword("password");
        student.setRole("TEACHER");
        userController.add(student);
        student.setPassword("password");

        try {
            jwtToken = userController.authenticate(student).getjwtToken();
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    	
        given()
                    .when()
                    .header("Authorization", "Bearer " + jwtToken)
                    .delete("exercise/1")
                    .then()
                    .statusCode(200);

    
     }
     @Test
     @Order(2)
     void TestGetAllExercises(){
   	            
         exercisecontroller.listExercises();
     	given()
                     .when()
                     .get("exercise/")
                     .then()
                     .statusCode(200);

    
    }

    @Test
    @Order(4)
    void TestGetOneExercise(){
        exercisecontroller.listExercises();
     
    	given()
                    .when()
                    .get("exercise/one/2")
                    .then()
                    .statusCode(200);

    
    }
    
 
}   	
