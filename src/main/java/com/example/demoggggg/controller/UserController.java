package com.example.demoggggg.controller;
import com.example.demoggggg.jwt.JWTRequest;
import com.example.demoggggg.jwt.JWTResponse;
import com.example.demoggggg.jwt.MyJwt;
import com.example.demoggggg.model.*;
import com.example.demoggggg.config.SecurityConfiguration.*;
import com.example.demoggggg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.demoggggg.model.enums.RoleEnum;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MyJwt jwt;
    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/teacher")
    public ResponseEntity<Teacher> add(@RequestBody Teacher teacher){

        teacher.setRole("TEACHER");
        Teacher newTeacher = userService.saveTeacher(teacher);

        return new ResponseEntity<>(newTeacher,HttpStatus.CREATED);
    }

    @PostMapping("/student")
    public ResponseEntity<Student> add(@RequestBody Student student){
        student.setRole("STUDENT");
        Student student1 = userService.saveStudent(student);
        return new ResponseEntity<>(student1,HttpStatus.CREATED);
    }

    /**
     * Narazie niech bedzie tak
     * @return
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public JWTResponse authenticate(@RequestBody UserEntity userEntity)throws Exception{
        String token = null;

        String password = userEntity.getPassword();
        String name = userEntity.getName();
        System.out.println(name);
        System.out.println(password);
        UserEntity entity = userService.findUserByNameAndPassword(name,password);
        if(entity!=null)
            System.out.println(entity.getRole());
        UserDetails userDetails=new User(name,password,List.of(new SimpleGrantedAuthority("ROLE_" +entity.getRole()))) ;
        token = jwt.generateToken(userDetails);

        try {
            System.out.println(userDetails.getAuthorities());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails, userDetails.getAuthorities());
            System.out.println(usernamePasswordAuthenticationToken.isAuthenticated());
            authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        }
        catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

        return new JWTResponse(token);
    }
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


    @GetMapping("/exercises/{name}")
    public List<Exercise> getExercises(@PathVariable String name){
        Teacher teacher = userService.findTeacherByName(name);

        if(teacher!=null) {
            return userService.findTeacherByName(name).getExercises();

        }
        return new ArrayList<>();
    }
    @GetMapping("/delete/{id}")
    public ResponseEntity<Long> delete(@PathVariable long id){
        long deleted = userService.deleteUser(id);
        return ResponseEntity.ok(deleted);
    }

    @PostMapping("/login")
    @Operation(summary = "Login based on user role after authentication", security = @SecurityRequirement(name = "bearerAuth"))
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
