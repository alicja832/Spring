package com.example.demoggggg.controller;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    private ExerciseController exerciseController;
    private int userId;
    private String role;
    private Student loginStudent;
    private Teacher loginTeacher;

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/teacher")

    public Teacher add(@RequestBody Teacher teacher){

        Teacher newTeacher = new Teacher();
        newTeacher.setName(teacher.getName());
        newTeacher.setEmail(teacher.getEmail());
        newTeacher.setPassword(teacher.getPassword());
        newTeacher= userService.saveTeacher(teacher);

        userId = newTeacher.getId();
        role="Teacher";
        loginTeacher = newTeacher;

        return newTeacher;
    }

    @PostMapping("/student")
    public String add(@RequestBody Student student){
        userService.saveStudent(student);
        loginStudent = student;
        userId = student.getId();
        role = "Uczeń";
        return "New student is added";
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
        //narazie głupi sposób
        if (loginStudent == null)
            return null;
        List<Student> students = List.of(loginStudent);
        return students;

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

    @PostMapping("/solution")
    public Solution addSolution(@RequestBody Solution solution){

        if(loginStudent!=null) {
            solution.setStudent(loginStudent);

            if (loginStudent.getSolutions().contains(solution)) {
                int ind = loginStudent.getSolutions().indexOf(solution);
                int score = loginStudent.getSolutions().get(ind).getScore();
                loginStudent.getSolutions().remove(solution);
                loginStudent.setScore(loginStudent.getScore() - score);
                loginStudent.getSolutions().add(solution);
                loginStudent.setScore(solution.getScore());
                return solution;
            } else {
                loginStudent.getSolutions().add(solution);
                loginStudent.setScore(solution.getScore());
            }
        }
        return userService.saveSolution(solution);

    }

    @GetMapping("/exercises")
    public List<Exercise> getExercises(){

        if( loginTeacher!=null)
            return loginTeacher.getExercises();
        return new ArrayList<>();
    }

    @GetMapping("/logout")
    public void logout(){
        loginStudent = null;
        loginTeacher = null;
    }

}
