package com.example.pythonapp.controller;
import javafx.util.Pair;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.service.ExerciseService;
import com.example.pythonapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@RestController
@CrossOrigin(origins = "http://localhost:3000/", maxAge=360000)
@RequestMapping("/exercise")
public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private UserService userService;
    
    /**
     * add new exercise
     * @param exercise
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<Exercise> add(@RequestBody Exercise exercise){

        exercise.setCorrectOutput(exerciseService.getOut("\n"+exercise.getCorrectSolution()+"\n"));
        if(exercise.getCorrectOutput().contains("Error"))
            return new ResponseEntity<>(exercise, HttpStatus.BAD_REQUEST);
        if(exercise.getCorrectOutput().contains("TraceBack"))
            return new ResponseEntity<>(exercise, HttpStatus.BAD_REQUEST);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = userService.findTeacherByName(name);
        exercise.setTeacher(teacher);

        try {
            exercise = exerciseService.save(exercise);
        }
        catch(Exception exception)
        {
            System.out.println(exercise);
            return new ResponseEntity<>(exercise, HttpStatus.BAD_REQUEST);
        }
           

        teacher.addExercise(exercise);

        return new ResponseEntity<>(exercise, HttpStatus.CREATED);
    }

    /**
     * delete exercise
     * @param id
     * @return
     */
    @CrossOrigin
    @DeleteMapping("/{id}")
    public ResponseEntity<Exercise> delete(@PathVariable int id){

	    Exercise exercise = exerciseService.findExerciseById(id);
        exerciseService.delete(id);
        exercise.getTeacher().removeExercise(exercise);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }
    /**
     *
     * update excercise
     * @param exercise
     */
    @PutMapping
    public void update(@RequestBody Exercise exercise){

        exercise.setCorrectOutput(exerciseService.getOut("\""+exercise.getCorrectSolution()+"\""));
        Teacher teacher = exerciseService.findExerciseById(exercise.getId()).getTeacher();
        exercise.setTeacher(teacher);
        exerciseService.update(exercise.getId(), exercise);
        Exercise newExercise = exerciseService.findExerciseById(exercise.getId());
        teacher.updateExercise(newExercise);
    }

    /**
     * get all exercises
     * @return
     * all exercises from database
     */

    private void setExercises()
    {
        //uwaga to na sztywno dodamy do bazy danych
        Teacher teacher = new Teacher();
        teacher.setEmail("alicja.zosia.k@gmail.com");
        teacher.setName("FirstTeacher");
        teacher.setPassword("FirstTeacher");
        teacher.setRole("Teacher");
        Exercise secondExercise = new Exercise();
        secondExercise.setName("Sortowanie przez wybieranie");
        secondExercise.setIntroduction(
                "Jedna z prostszych metod sortowania o złożoności O(n*n).Polega na wyszukaniu elementu mającego się znaleźć na żądanej pozycji " +
                        "i zamianie miejscami z tym, który jest tam obecnie. Operacja jest wykonywana dla wszystkich indeksów sortowanej tablicy.");
        secondExercise.setContent("Zaimplementuj sortowanie przez wybieranie w pythonie." +
                " Wywołaj metodę dla następującej tablicy:[-1,0,10,-1,14,4,6,5]. Wypisz jej zawaratość w takiej samej postaci po posortwaniu.");
        secondExercise.setCorrectSolution("A = [-1,0,10,-1,14,4,6,5]\nn=len(A)\nfor i in range(n):\n\tmin=i\n\tfor j in range(i,n):\n\t\tif(A[j]<A[min]):\n\t\t\tmin = j\n\ttmp=A[i]\n\tA[i]=A[min]\n\tA[min]=tmp\nprint(A)");
        secondExercise.setCorrectOutput(this.getresponse("A = [-1,0,10,-1,14,4,6,5]\nn=len(A)\nfor i in range(n):\n\tmin=i\n\tfor j in range(i,n):\n\t\tif(A[j]<A[min]):\n\t\t\tmin = j\n\ttmp=A[i]\n\tA[i]=A[min]\n\tA[min]=tmp\nprint(A)"));
        secondExercise.setMaxPoints(5);
        if(userService.findTeacherByEmail("alicja.zosia.k@gmail.com") == null)
        {
            userService.saveTeacher(teacher);
        }
        else
        {
            teacher = userService.findTeacherByEmail("alicja.zosia.k@gmail.com");
        }
        secondExercise.setTeacher(teacher);
        exerciseService.save(secondExercise);
    }
   
    @GetMapping("/")
    public List<Pair<Exercise,Boolean>> listExercises(){

        String name = "";
        if(SecurityContextHolder.getContext().getAuthentication()!=null)
            name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(exerciseService.getAllExercises().isEmpty() || exerciseService.findExerciseByName("Sortowanie przez wybieranie")==null)
            setExercises();
        List<Solution> solutions;
        Student student = userService.findStudentByName(name);
        if(student!=null)
        {
            solutions = exerciseService.findStudentSolution(student);
        }
        else {
            solutions = new ArrayList<>();
        }

        List<Exercise> list = exerciseService.getAllExercises();
        List<Pair<Exercise,Boolean>> listExercise = new ArrayList<>();

        for(Exercise e:list)
        {
            Predicate<Solution> function = (solution) ->solution.getExercise().equals(e);
            boolean flag = solutions.stream().anyMatch(function);
            if(flag)
                listExercise.add(new Pair<Exercise,Boolean>(e,true));
            else
                listExercise.add(new Pair<Exercise,Boolean>(e,false));
        }

        return listExercise;
    }
    /**
     * get one exercise
     * @param id
     * @return
     */
    @GetMapping("/one/{id}")
    public List<Exercise> FindById(@PathVariable int id) {

        List<Exercise> exercises = List.of(exerciseService.findExerciseById(id));
        return exercises;
    }
    /**
     * get response from python interpreter
     * @param text
     * @return
     */
    @PostMapping("/interpreter")
    public String getresponse(@RequestBody String text){
        String str = exerciseService.getOut(text);
        return str;
    }
    /**
     * get one's solutions with name exercise and score
     **/
    @GetMapping("/solutions")
    @CrossOrigin(origins = "http://localhost:3000/profil")
    public List<Map<String,String>> getSolutions(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Map<String, String>> excercisesAndScores = new ArrayList<>();
        List<Solution> solutions = exerciseService.findStudentSolution(userService.findStudentByName(name));

        for(Solution solution:solutions) {

            Map<String,String> map = new HashMap<>();
            Exercise exercise=solution.getExercise();
            if(exercise!=null){
                map.put("id", Integer.toString(solution.getId()));
                map.put("name",solution.getExercise().getName());
                map.put("score", Integer.toString(solution.getScore()));
            }
            excercisesAndScores.add(map);
        }

        return excercisesAndScores;
    }
      /**
     * add one's solutions with name exercise and score
     **/
    @PostMapping("/solution")
    public ResponseEntity<Solution> addSolution(@RequestBody Solution solution){

        Student loginStudent = userService.findStudentByName(SecurityContextHolder.getContext().getAuthentication().getName());
        if(solution.getScore()==0)
        {
            this.checkSolution(solution);
        }
        if(solution.getOutput()==null)
        {
            solution.setOutput(getresponse(solution.getSolutionContent()));
        }

        exerciseService.save(solution);
        userService.updateStudent(userService.findStudentByName(loginStudent.getId(),loginStudent.getScore()+solution.getScore()));
        loginStudent.addSolution(solution);

        return new ResponseEntity<>(solution,HttpStatus.CREATED);

    }
    /**
     * get one solution from database
     * @param id
     * @return
     */
    @GetMapping("/solution/{id}")
    public List<Solution> getSolution(@PathVariable int id){

        Solution solution = exerciseService.findSolutionById(id);
        return List.of(solution);
    }

    /**
     * function to update one solution by one user
     * @param solution new version of solution
     */
    @PutMapping("/solution")
    public void updateSolution(@RequestBody Solution solution){

        int oldScore = exerciseService.findSolutionById(solution.getId()).getScore();
        exerciseService.updateSolution(solution.getId(),solution);
        Student student = userService.findStudentByName();
        student.setScore(student.getScore()-oldScore+solution.getScore());

    }

    /**
     * function which check the solution and change the user's score
     * @param solution - solution to check
     * @return
     */
    @PostMapping("/check")

    public int checkSolution(@RequestBody Solution solution){

        String expectedOutput = solution.getExercise().getCorrectOutput();
        int maxPoints =  solution.getExercise().getMaxPoints();

        if(expectedOutput.equals(solution.getOutput()) || expectedOutput.trim().equals(solution.getOutput().trim()))
        {
            solution.setScore(maxPoints);
            return maxPoints;
        }

        return 0;
    }

}
