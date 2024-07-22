package com.example.demoggggg.controller;

import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.service.ExerciseService;
import com.example.demoggggg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin("*")
@RestController
@RequestMapping("/exercise")

public class ExerciseController {
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private UserController userController;

    /**
     * add new exercise
     * @param exercise
     * @return
     */
    @PostMapping("/")
    public Exercise add(@RequestBody Exercise exercise){

        Exercise savedExercise = new Exercise();

        savedExercise.setContent(exercise.getContent());
        savedExercise.setName(exercise.getName());
        savedExercise.setIntroduction(exercise.getIntroduction());
        savedExercise.setCorrectSolution(exercise.getCorrectSolution());
        savedExercise.setMaxPoints(exercise.getMaxPoints());
        savedExercise.setCorrectOutput(exerciseService.getOut("\n"+exercise.getCorrectSolution()+"\n"));

        exerciseService.save(savedExercise);
        userController.addExercise(savedExercise);

        return savedExercise;
    }

    /**
     * delete exercise
     * @param id
     * @return
     */
    @PostMapping("/delete/{id}")
    public void delete(@PathVariable int id){

        userController.deleteExercise(exerciseService.findById(id));
        exerciseService.delete(id);
    }

    /**
     * update excercise
     * @param exercise
     */
    @PutMapping("/")
    public void update(@RequestBody Exercise exercise){

        exercise.setCorrectOutput(exerciseService.getOut("\""+exercise.getCorrectSolution()+"\""));
        exerciseService.update(exercise.getId(), exercise);
        Exercise newExercise = exerciseService.findById(exercise.getId());

        if(userController.getTeacher()!=null)
            userController.getTeacher().get(0).getExercises().set( userController.getTeacher().get(0).getExercises().indexOf(exercise),newExercise);

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

    /**
     * get one exercise
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public List<Exercise> FindById(@PathVariable int id) {
        List<Exercise> exercises = List.of(exerciseService.findById(id));
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
     * TODO Add posibility to list all excercises which are solved by user and score from its
     **/
    @GetMapping("/solutions")
    public List<Map<String,String>> getSolutions(){

        List<Map<String, String>> excercisesAndScores = new ArrayList<>();
        List<Solution> solutions=exerciseService.findStudentSolution(userController.getStudent().get(0));

        for(Solution solution:solutions){

            Map<String,String> map = new HashMap<String,String>();
            Exercise exercise=exerciseService.findById(solution.getExercise().getId());
            if(exercise!=null){
                map.put("id", Integer.toString(solution.getId()));
                map.put("name",solution.getExercise().getName());
                map.put("maxPoints", Integer.toString(solution.getScore()));
            }
            excercisesAndScores.add(map);
        }
        return excercisesAndScores;
    }
    @GetMapping("/solution/{id}")
    public List<Solution> getSolution(@PathVariable int id){

        return List.of(exerciseService.findByI(id));
    }

    @PutMapping("/solution")
    public void updateSolution(@RequestBody Solution solution){

        exerciseService.updateSolution(solution.getId(),solution);
        if(!userController.getStudent().isEmpty())
            userController.getStudent().get(0).getSolutions().set( userController.getStudent().get(0).getSolutions().indexOf(solution),getSolution(solution.getId()).get(0));

    }

    @PostMapping("/check")
    public int checkSolution(@RequestBody Solution solution){

        String expectedOutput = solution.getExercise().getCorrectOutput();
        int maxPoints =  solution.getExercise().getMaxPoints();

        if(expectedOutput.equals(solution.getOutput()) || expectedOutput.trim().equals(solution.getOutput().trim()))
        {
            int points = maxPoints - solution.getScore();
            userController.getStudent().get(0).addPoints(points);
            return maxPoints;
        }

        return 0;
    }

}