package com.example.demoggggg.controller;

import com.example.demoggggg.dto.ExerciseDto;
import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
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

       exercise.getTeacher().AddExc(savedExercise);

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
    @DeleteMapping("/solution/{id}")
    public ResponseEntity<Long> deleteSolution(@PathVariable long id){

        exerciseService.deleteSolutionById(id);
        exerciseService.deleteSolutionById(id);
        return ResponseEntity.ok(id);
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
    @GetMapping("/{name}")
    public Map<Exercise,Boolean> listExercises(@PathVariable String name){
        List<Solution> solutions = userService.findStudentByName(name).getSolutions();
        List<Exercise> list= exerciseService.getAllExercises();
        Map<Exercise,Boolean> listExercise = new HashMap<>();
        for(Exercise e:list)
        {

            Predicate<Solution> function = (solution) ->solution.getExercise().equals(e);
            boolean flag = solutions.stream().anyMatch(function);
            if(flag)
                listExercise.put(e,true);
            else
                listExercise.put(e,false);
        }
        return listExercise;
    }

    /**
     * get one exercise
     * @param id
     * @return
     */
    @GetMapping("/{id}")
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
        List<Solution> solutions=exerciseService.findStudentSolution(userService.findStudentByName(name));

        for(Solution solution:solutions){
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
     

        Student loginStudent = userService.findStudentByName(solution.getStudent().getName());
        solution.setStudent(loginStudent);
        if(solution.getScore()==0)
        {
            this.checkSolution(solution);
        }
        if (loginStudent.getSolutions().contains(solution)) {
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

        return List.of(exerciseService.findSolutionById(id));
    }

    /**
     * function to update one solution by one user
     * @param solution new version of solution
     */
    @PutMapping("/solution")
    public void updateSolution(@RequestBody Solution solution){

        exerciseService.updateSolution(solution.getId(),solution);
        solution.getStudent().updateSolution(solution);

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