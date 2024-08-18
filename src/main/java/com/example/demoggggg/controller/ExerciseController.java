package com.example.demoggggg.controller;
import javafx.util.Pair;
import com.example.demoggggg.dto.ExerciseDto;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import com.example.demoggggg.service.ExerciseService;
import com.example.demoggggg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
//localhost na porcie 3000

@RestController
@RequestMapping("/exercise")
@CrossOrigin(origins = "http://localhost:3000")
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

        savedExercise = exerciseService.save(savedExercise);
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

        exerciseService.findExerciseById(id).getTeacher().removeExercise(exerciseService.findExerciseById(id));
        exerciseService.delete(id);
    }
    /**
     *
     * update excercise
     * @param exercise
     */
    @PutMapping
    public void update(@RequestBody Exercise exercise){

        exercise.setCorrectOutput(exerciseService.getOut("\""+exercise.getCorrectSolution()+"\""));
        exerciseService.update(exercise.getId(), exercise);
        Exercise newExercise = exerciseService.findExerciseById(exercise.getId());
        exercise.getTeacher().updateExercise(newExercise);
    }
    /**
     * get all exercises
     * @return
     * all exercises from database
     */
    @GetMapping("/")
    public List<Exercise> listExercises(){
        return exerciseService.getAllExercises();
    }

    @GetMapping("/{email}")
    public List<Pair<Exercise,Boolean>> listExercises(@PathVariable String email){

        List<Solution> solutions;
        Student student = userService.findStudentByEmail(email);

        if(student!=null)
        {
            solutions = exerciseService.findStudentSolution(student);
        }
        else {

            solutions = new ArrayList<Solution>();
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

        return exerciseService.getOut(text);
    }
    /**
     * get one's solutions with name exercise and score
     **/
    @GetMapping("/solutions/{name}")
    public List<Map<String,String>> getSolutions(@PathVariable String name){

        List<Map<String, String>> excercisesAndScores = new ArrayList<>();
        List<Solution> solutions = exerciseService.findStudentSolution(userService.findStudentByName(name));

        for(Solution solution:solutions) {
            System.out.println(solution.getScore());
            Map<String,String> map = new HashMap<String,String>();
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
    @PostMapping("/solution")
    public ResponseEntity<Solution> addSolution(@RequestBody Solution solution){


        Student loginStudent = userService.findStudentByEmail(solution.getStudentEmail());

        if(solution.getScore()==0)
        {
            this.checkSolution(solution);
        }
        if (loginStudent.getSolutions().contains(solution)) {

            System.out.println(loginStudent.getEmail());
            int ind = loginStudent.getSolutions().indexOf(solution);
            int score = loginStudent.getSolutions().get(ind).getScore();

            loginStudent.getSolutions().remove(solution);
            loginStudent.setScore(loginStudent.getScore() - score);
            loginStudent.getSolutions().add(solution);
            loginStudent.addPoints(solution.getScore());

            return new ResponseEntity<>(solution,HttpStatus.CREATED);
        } else {

            exerciseService.save(solution);
            loginStudent.addSolution(solution);
            loginStudent.addPoints(solution.getScore());
            System.out.println("Dodawanie rozwiazan");
            System.out.println(loginStudent.getSolutions().toString());
        }

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