package com.example.pythonapp.controller;
import com.example.pythonapp.exception.ExerciseNotFoundException;
import com.example.pythonapp.service.*;
import javafx.util.Pair;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
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
    private SolutionService solutionService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private StudentService studentService;
    
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
        Teacher teacher = teacherService.findByName(name).get();
        exercise.setTeacher(teacher);

        try {
            exerciseService.save(exercise);
        }catch(Exception e){
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

	    Exercise exercise = exerciseService.findExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
        exerciseService.delete(id);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }
    /**
     * update excercise
     * @param exercise
     */
    @PutMapping
    public void update(@RequestBody Exercise exercise){

        exercise.setCorrectOutput(exerciseService.getOut("\""+exercise.getCorrectSolution()+"\""));
        Exercise exercisefound = exerciseService.findExerciseById(exercise.getId()).orElseThrow(ExerciseNotFoundException::new);
        Teacher teacher = exercisefound.getTeacher();
        exercise.setTeacher(teacher);
        exerciseService.update(exercise.getId(), exercise);
        Exercise newExercise = exerciseService.findExerciseById(exercise.getId()).orElseThrow(ExerciseNotFoundException::new);
        teacher.updateExercise(newExercise);
    }

    @GetMapping("/")
    public List<Pair<Exercise,Boolean>> listExercises(){

        List<Exercise> list = exerciseService.getAllExercises();
        List<Pair<Exercise,Boolean>> listExercise = new ArrayList<>();
        List<Solution> solutions = new ArrayList<>();

        if(SecurityContextHolder.getContext().getAuthentication()!=null) {

            if(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
              solutions = solutionService.findStudentSolution(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        }

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

        Exercise exercise = exerciseService.findExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
        return List.of(exercise);
    }
    /**
     * get response from python interpreter
     * @param text
     * @return
     */
    @PostMapping("/interpreter")
    public String getresponse(@RequestBody String text){
        return exerciseService.getOut(text);
    }

    /**
     * get one's solutions with name exercise and score
     **/
    @GetMapping("/solutions")
    public List<Map<String,String>> getSolutions(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Map<String, String>> excercisesAndScores = new ArrayList<>();
        List<Solution> solutions = solutionService.findStudentSolution(studentService.findByName(name).get());

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

        Student loginStudent = studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if(solution.getScore()==0)
        {
            this.checkSolution(solution);
        }
        if(solution.getOutput()==null)
        {
            solution.setOutput(getresponse(solution.getSolutionContent()));
        }
        solution.setStudent(loginStudent);
        solutionService.save(solution);
        studentService.update(loginStudent.getId(),loginStudent.getScore()+solution.getScore());

        return new ResponseEntity<>(solution,HttpStatus.CREATED);

    }
    /**
     * get one solution from database
     * @param id
     * @return
     */
    @GetMapping("/solution/{id}")
    public List<Solution> getSolution(@PathVariable int id){

        return List.of(solutionService.findById(id));
    }

    /**
     * function to update one solution by one user
     * @param solution new version of solution
     */
    @PutMapping("/solution")
    public void updateSolution(@RequestBody Solution solution){
        int oldScore = solutionService.findById(solution.getId()).getScore();
        solutionService.update(solution.getId(),solution);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Student loginStudent = studentService.findByName(name).get();
        studentService.update(loginStudent.getId(),loginStudent.getScore()-oldScore+solution.getScore());
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
