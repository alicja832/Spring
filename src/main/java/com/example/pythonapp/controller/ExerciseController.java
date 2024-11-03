package com.example.pythonapp.controller;
import javafx.util.Pair;
import com.example.pythonapp.dto.ExerciseDto;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.service.ExerciseService;
import com.example.pythonapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

@CrossOrigin(origins = "http://localhost:3000/", maxAge=360000)
@RestController
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
    public ResponseEntity<Exercise> add(@RequestBody ExerciseDto exercise){

        Exercise savedExercise = new Exercise();
        savedExercise.setContent(exercise.getContent());
        savedExercise.setName(exercise.getName());
        savedExercise.setIntroduction(exercise.getIntroduction());
        savedExercise.setCorrectSolution(exercise.getCorrectSolution());
        savedExercise.setMaxPoints(exercise.getMaxPoints());
        savedExercise.setTeacher(exercise.getTeacher());
        savedExercise.setCorrectOutput(exerciseService.getOut("\n"+exercise.getCorrectSolution()+"\n"));
        try {
            savedExercise = exerciseService.save(savedExercise);
        }
        catch(Exception exception)
        {
            System.out.println(exercise);
            return new ResponseEntity<>(savedExercise, HttpStatus.BAD_REQUEST);
        }
            if(savedExercise.getCorrectOutput().contains("Error"))
                    return new ResponseEntity<>(savedExercise, HttpStatus.BAD_REQUEST);
            if(savedExercise.getCorrectOutput().contains("TraceBack"))
                        return new ResponseEntity<>(savedExercise, HttpStatus.BAD_REQUEST);
        Teacher teacher = userService.findTeacherByName(exercise.getTeacher().getName());
        teacher.AddExc(savedExercise);

        return new ResponseEntity<>(savedExercise, HttpStatus.CREATED);
    }

    /**
     * delete exercise
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id){

	    Exercise exercise = exerciseService.findExerciseById(id);
        exerciseService.delete(id);
        exercise.getTeacher().removeExercise(exercise);
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
    @GetMapping("/")
    public List<Pair<Exercise,Boolean>> listExercises(){

       if(exerciseService.getAllExercises().isEmpty() || exerciseService.findExerciseByName("Sortowanie przez wybieranie")==null)
           setExercises();
        List<Pair<Exercise,Boolean>> listExercise = new ArrayList<>();
        for(Exercise e:exerciseService.getAllExercises())
        {
            listExercise.add(new Pair<>(e,false));
        }
        return listExercise;
    }
    private void setExercises()
    {
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
        System.out.println(secondExercise.getCorrectOutput());
        exerciseService.save(secondExercise);
    }
    
    @GetMapping("/{email}")
    public List<Pair<Exercise,Boolean>> listExercises(@PathVariable String email){

        if(exerciseService.getAllExercises().isEmpty() || exerciseService.findExerciseByName("Sortowanie przez wybieranie")==null)
            setExercises();
        List<Solution> solutions;
        Student student = userService.findStudentByEmail(email);
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
    @GetMapping("/solutions/{name}")
    public List<Map<String,String>> getSolutions(@PathVariable String name){

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


        Student loginStudent = userService.findStudentByEmail(solution.getStudentEmail());
        if(solution.getScore()==0)
        {
            this.checkSolution(solution);
        }
        if(solution.getOutput()==null)
        {
            solution.setOutput(getresponse(solution.getSolutionContent()));
        }


            exerciseService.save(solution);
            loginStudent.addSolution(solution);
            loginStudent.addPoints(solution.getScore());
        

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
        Student student = userService.findStudentByEmail(solution.getStudentEmail());
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
