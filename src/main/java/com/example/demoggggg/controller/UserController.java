package com.example.demoggggg.controller;
import com.example.demoggggg.model.*;
import com.example.demoggggg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

//import com.example.demoggggg.jwt.JWTRequest;
//import com.example.demoggggg.jwt.JWTResponse;
//import com.example.demoggggg.jwt.MyJwt;
//
//import com.example.demoggggg.config.SecurityConfiguration.*;
//
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//import com.example.demoggggg.model.enums.RoleEnum;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.core.userdetails.User;


@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;
    private long userId;
    private String role;
    private Teacher loginTeacher;
    private Student loginStudent;

//    @Autowired
//    private MyJwt jwt;
//    @Autowired
//    private AuthenticationManager authenticationManager;


    @PostMapping("/teacher")

    public Teacher add(@RequestBody Teacher teacher){

        teacher.setRole("TEACHER");
        Teacher newTeacher= userService.saveTeacher(teacher);

        userId = newTeacher.getId();
        role="Teacher";
        loginTeacher = newTeacher;


        return newTeacher;
    }

    @PostMapping("/student")
    public Student add(@RequestBody Student student){
        student.setRole("STUDENT");

        loginStudent = userService.saveStudent(student);
        userId = student.getId();
        role = "Uczeń";

        return student;
    }

    @PostMapping("/loginTeacher")
    public ResponseEntity<String> login(@RequestBody Teacher teacher)  {

        userId = userService.loginTeacher(teacher);
        if (userId == -1)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The user doesn't exist");
        }
        role="Teacher";
        loginTeacher = teacher;
        return ResponseEntity.ok("The user was logged");
    }

    @PostMapping("/loginStudent")
    public  ResponseEntity<String> login(@RequestBody Student student){

        userId = userService.loginStudent(student);
        if (userId == -1)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("The user doesn't exist");
        }
        role = "Uczeń";
        loginStudent = student;
        return ResponseEntity.ok("The user was logged");
    }

    @GetMapping("/role")
    public ResponseEntity<String> getRole()
    {
        //narazie głupi sposób
        return ResponseEntity.ok(role);

    }
    @GetMapping("/student")
    public List<Student> getStudent()
    {
        System.out.println(loginStudent.toString());
        Student tmp = new Student();
        tmp.setScore(loginStudent.getScore());
        tmp.setEmail(loginStudent.getEmail());
        tmp.setPassword(loginStudent.getPassword());
        tmp.setName(loginStudent.getName());
        //narazie głupi sposób
        if (loginStudent == null)
            return null;
        return List.of(tmp);

    }
    @GetMapping("/teacher")
    public List<Teacher> getTeacher()
    {
        //narazie głupi sposób
        if (loginTeacher == null)
            return null;
        List<Teacher> teachers = List.of(loginTeacher);
        return teachers;

    }

    public void addExercise(@RequestBody Exercise exercise)  {
        if(loginTeacher!=null)
            loginTeacher.AddExc(exercise);
    }
    public void deleteExercise(@RequestBody Exercise exercise)  {
        loginTeacher.getExercises().remove(exercise);
    }

//    @PostMapping("/solution")
//    public Solution addSolution(@RequestBody Solution solution){
//
//
//            if (loginStudent.getSolutions().contains(solution)) {
//                int ind = loginStudent.getSolutions().indexOf(solution);
//                int score = loginStudent.getSolutions().get(ind).getScore();
//                loginStudent.getSolutions().remove(solution);
//                loginStudent.setScore(loginStudent.getScore() - score);
//                loginStudent.getSolutions().add(solution);
//                loginStudent.setScore(solution.getScore());
//                return solution;
//            } else {
//                loginStudent.getSolutions().add(solution);
//                loginStudent.setScore(solution.getScore());
//            }
//
//        return userService.saveSolution(solution);
//
//    }

    @GetMapping("/exercises")
    public List<Exercise> getExercises(){

        if( loginTeacher!=null) {
            loginTeacher = userService.findTeacherByName(loginTeacher.getName());
            System.out.printf(loginTeacher.getExercises().toString());
            return loginTeacher.getExercises();

        }
        return new ArrayList<>();
    }

    @GetMapping("/logout")
    public void logout(){
        loginStudent = null;
        loginTeacher = null;
    }
    /**
     * Narazie niech bedzie tak
     * @return
     * @throws Exception
     */
//    @PostMapping("/authenticate")
//    public JWTResponse authenticate(@RequestBody UserEntity userEntity)throws Exception{
//        String token = null;
//
//
//
//        String password = userEntity.getPassword();
//        String name = userEntity.getName();
//        System.out.println(name);
//        System.out.println(password);
//        UserEntity entity = userService.findUserByNameAndPassword(name,password);
//        if(entity!=null)
//            System.out.println(entity.getRole());
//        UserDetails userDetails=new User(name,password,List.of(new SimpleGrantedAuthority("ROLE_" +entity.getRole()))) ;
//        token = jwt.generateToken(userDetails);
//
//        try {
//            System.out.println(userDetails.getAuthorities());
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(userEntity.getName(), userEntity.getPassword())
//            );
//
//            System.out.println("Authentication successful: " + authentication.getName());
//        }
//        catch (BadCredentialsException e) {
//            throw new Exception("INVALID_CREDENTIALS", e);
//        }
//        catch(Exception ex)
//        {
//            System.out.println(ex.getMessage());
//        }
//        System.out.println("OK");
//        return new JWTResponse(token);
//    }
    @GetMapping("/teacher/{name}")
    public Teacher get(@PathVariable String name){

        return userService.findTeacherByName(name);
    }

    @GetMapping("/student/{name}")
    public Student getStudent(@PathVariable String name){

        return userService.findStudentByName(name);

    }
    @GetMapping("/users")
    public List<UserEntity> list(){

        return userService.listAll();
    }



    @GetMapping("/delete/{id}")
    public ResponseEntity<Long> delete(@PathVariable long id){

        long deleted = userService.deleteUser(id);
        return ResponseEntity.ok(deleted);
    }

    @PostMapping("/login")
    //  @Operation(summary = "Login based on user role after authentication", security = @SecurityRequirement(name = "bearerAuth"))
    public String login(@RequestBody UserEntity user) {

        System.out.println(user.getPassword());
        UserEntity userByUsername = this.userService.findByName(user.getName());

        switch(userByUsername.getRole()) {
            case "STUDENT":
                return "STUDENT";
            case "TEACHER":
                return "TEACHER";
            case "ADMIN":
                return "ADMIN";
        }
        return null;
    }
}