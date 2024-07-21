package com.example.demoggggg.controller;

import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.service.ExerciseService;
import com.example.demoggggg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/exercise")
@CrossOrigin
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

        exerciseService.save(savedExercise);
        userController.addExercise(savedExercise);

        return savedExercise;
    }

    /**
     * delete exercise
     * @param id
     * @return
     */
    @PostMapping("/delete")
    public void delete(@RequestBody int id){

        userController.deleteExercise(exerciseService.findById(id));
        exerciseService.delete(id);
    }

    /**
     * update excercise
     * @param exercise
     */
    @PostMapping("/update")
    public void update(@RequestBody Exercise exercise){
        exerciseService.update(exercise.getId(), exercise);
        Exercise newExercise = exerciseService.findById(exercise.getId());
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
    @GetMapping("/id")
    public List<Exercise> FindById(@RequestParam("id") int id) {
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
    public List<List<String>> getSolutions(@RequestBody int id){

        List<List<String>> excercisesAndScores = new ArrayList<>();
        List<Solution> solutions=exerciseService.findStudentSolution(userController.getUserService().findbyId(id));

        for(Solution solution:solutions){

            List<String> solutionList = new ArrayList<>();
            Exercise exercise=exerciseService.findById(solution.getExercise().getId());
            if(exercise!=null){
                solutionList.add(solution.getExercise().getName());
                solutionList.add( Integer.toString(solution.getScore()));
            }
            excercisesAndScores.add(solutionList);
        }
        return excercisesAndScores;
    }

}