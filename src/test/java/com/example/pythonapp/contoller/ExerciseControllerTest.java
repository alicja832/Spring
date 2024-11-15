//package com.example.pythonapp.contoller;
//import com.example.pythonapp.controller.ExerciseController;
//import com.example.pythonapp.controller.UserController;
//import com.example.pythonapp.dto.UserCreationDto;
//import com.example.pythonapp.model.Solution;
//import com.example.pythonapp.model.Student;
//import com.example.pythonapp.model.Teacher;
//import com.example.pythonapp.model.enums.Role;
//import com.example.pythonapp.service.ExerciseService;
//import com.example.pythonapp.service.StudentService;
//import com.example.pythonapp.service.TeacherService;
//import com.example.pythonapp.service.UserService;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//import static io.restassured.RestAssured.given;
//
//import io.restassured.RestAssured;
//
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class ExerciseTest {
//
//    @Autowired
//    ExerciseController exercisecontroller;
//    @Autowired
//    UserController userController;
//    String jwtToken;
//    @LocalServerPort
//    private Integer port;
//    @Autowired
//    private ExerciseService exerciseService;
//    @Autowired
//    private TeacherService teacherService;
//    @Autowired
//    private StudentService studentService;
//    @Autowired
//    private UserService userService;
//
//    @BeforeEach
//    void setUp() {
//        RestAssured.port = port;
//    }
//
//    @Test
//    @Order(2)
//    void pythonInterpeterTest() {
//
//        Assertions.assertEquals( "a\n",exercisecontroller.getresponse("print('a')"));
//        Assertions.assertEquals( "[-1, -1, 0, 4, 5, 6, 10, 14]\n",exercisecontroller.getresponse("A = [-1,0,10,-1,14,4,6,5]\nn=len(A)\nfor i in range(n):\n\tmin=i\n\tfor j in range(i,n):\n\t\tif(A[j]<A[min]):\n\t\t\tmin = j\n\ttmp=A[i]\n\tA[i]=A[min]\n\tA[min]=tmp\nprint(A)"));
//        Assertions.assertEquals( 1,exercisecontroller.listExercises().size());
//        Assertions.assertEquals( "Traceback (most recent call last):\n  File \"<string>\", line 1, in <module>\nNameError: name 'a' is not defined\n\n\t",exercisecontroller.getresponse("a"));
//        Assertions.assertEquals( "a\n",exercisecontroller.getresponse("print('a')"));
//    }
//    @Test
//    @Order(1)
//    void TestDeleteExercise(){
//
//
//        UserCreationDto student = new UserCreationDto();
//        student.setEmail("teacherz@gmail.com");
//        student.setName("someonew");
//        student.setPassword("password");
//        student.setRole(Role.TEACHER);
//        userController.add(student);
//        student.setPassword("password");
//        exercisecontroller.listExercises();
//        try {
//            jwtToken = userController.authenticate().getJwtToken();
//        }
//        catch (Exception exception)
//        {
//            exception.printStackTrace();
//        }
//
//        given()
//                    .when()
//                    .header("Authorization", "Bearer " + jwtToken)
//                    .delete("exercise/1")
//                    .then()
//                    .statusCode(200);
//        Assertions.assertTrue(student.getExercises().isEmpty());
//
//     }
//     @Test
//     @Order(3)
//     void TestGetAllExercises(){
//
//
//     	given()
//                     .when()
//                     .get("exercise/")
//                     .then()
//                     .statusCode(200);
//
//
//    }
//
//    @Test
//    @Order(5)
//    void TestGetOneExercise(){
//        exercisecontroller.listExercises();
//
//    	given()
//                    .when()
//                    .get("exercise/one/2")
//                    .then()
//                    .statusCode(200);
//
//
//    }
//    @Test
//    @Order(4)
//    void TestaddSolution(){
//
//        Solution solution = new Solution();
//        solution.setSolutionContent("print(1)");
//        solution.setExercise(exercisecontroller.listExercises().get(0).getKey());
//
//
//        Student student = new Student();
//        student.setEmail("teacherddz@gmail.com");
//        student.setName("someoddnew");
//        student.setPassword("password");
//        student.setRole("STUDENT");
//        userController.add(student);
//        solution.setStudent(userService.findStudentByName(student.getName()));
//        exerciseService.save(solution);
//        student.setPassword("password");
//
//        try {
//            jwtToken = userController.authenticate(student).getJwtToken();
//        }
//        catch (Exception exception)
//        {
//            exception.printStackTrace();
//        }
//
//        given()
//                .when()
//                .header("Authorization", "Bearer " + jwtToken)
//                .get("exercise/solutions")
//                .then()
//                .statusCode(200);
//
//    }
//
//
//}
