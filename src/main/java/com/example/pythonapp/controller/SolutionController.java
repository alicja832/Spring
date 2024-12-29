package com.example.pythonapp.controller;
import javafx.util.Pair;
import com.example.pythonapp.PythonApplication;
import com.example.pythonapp.dto.LongSolutionDto;
import com.example.pythonapp.exception.ExerciseNotFoundException;
import com.example.pythonapp.mapper.LongSolutionMapper;
import com.example.pythonapp.model.*;
import com.example.pythonapp.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin (origins=PythonApplication.frontend,maxAge = 3600)
@RequestMapping("/solution")
public class SolutionController {

    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private SolutionService solutionService;
    @Autowired
    private StudentService studentService;
    private final LongSolutionMapper longSolutionMapper=new LongSolutionMapper();
    
    /**
     * get one's solutions with name exercise and score
     **/
    @GetMapping("/")
    public List<Map<String,String>> getSolutions(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Map<String, String>> exercisesAndScores = new ArrayList<>();
        if(studentService.findByName(name).isEmpty()) return exercisesAndScores;
        List<Solution> solutions = solutionService.findStudentSolution(studentService.findByName(name).get());

        for(Solution solution:solutions) {

            Map<String,String> map = new HashMap<>();
            Exercise exercise=solution.getExercise();


                    map.put("id", Integer.toString(solution.getId()));
                    map.put("name", solution.getExercise().getName());
                    map.put("score", Integer.toString(solution.getScore()));

                    if(exercise instanceof LongExercise) { map.put("retakeposibility", "true");}
                    else {map.put("retakeposibility", "false");}

            exercisesAndScores.add(map);
        }

        return exercisesAndScores;
    }
    
    /**
     * add one's programming solution with name exercise and score
     **/
    @PostMapping("/programming")
    public ResponseEntity<Solution> addSolution(@RequestBody LongSolutionDto solution){

        LongExercise exercise =  exerciseService.findLongExerciseById(solution.getExercise().getId()).get();
        solution.setExercise(exercise);
        LongSolutionMapper longSolutionMapper=new LongSolutionMapper();
        Student loginStudent = studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        int newscore = (exercise.getSolutionSchema().isEmpty()) ? checkSolution(solution) : checkSolutionWithTests(solution).getValue();
       
        LongSolution solutionCreated = longSolutionMapper.createLongSolution(solution);
        solutionCreated.setStudent(loginStudent);
        solutionCreated.setScore(newscore);
        
        try{
            solutionService.save(solutionCreated);
            studentService.update(loginStudent.getId(),solutionCreated.getScore());
        }catch(Exception exception)
        {
            System.out.println(exception.getMessage());
        }
        return new ResponseEntity<>(solutionCreated,HttpStatus.CREATED);
    }
    /**
     * add one's abc solution with name exercise and score
     **/
    @PostMapping("/abc")
    public ResponseEntity<Solution> addSolution(@RequestBody ShortSolution solution){

        Student loginStudent = studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        solution.setScore(checkSolution(solution));
        solution.setStudent(loginStudent);
        solutionService.save(solution);
        studentService.update(loginStudent.getId(),solution.getScore());
        
        return new ResponseEntity<>(solution,HttpStatus.CREATED);

    }
    /**
     * get one long solution from database
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public List<LongSolution> getLongSolution(@PathVariable int id){


        return List.of(solutionService.findLongSolutionById(id));
    }
    /**
     * get  Longcorrectsolutionparts from database for given long exercise
     */
     @GetMapping("/parts/{id}")
    public List<LongCorrectSolutionPart> getLongPart(@PathVariable int id){

        return  exerciseService.findAllLongCorrectSolutionByExerciseId(id);
     
    }
     /**
     * get one short solution from database
     * @param id
     * @return
     */
    @GetMapping("/abc/{id}")
    public List<ShortSolution> getShortSolution(@PathVariable int id){
        return List.of(solutionService.findShortSolutionById(id));
    }

    /**
     * function to update one solution by one user
     * @param solution new version of solution
     */
    @PutMapping("/")
    public ResponseEntity<String> updateSolution( @RequestBody LongSolutionDto solution){

        if(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Zalogowany użytkownik nie jest uczniem");
        solutionService.updateSolution(longSolutionMapper.createLongSolution(solution),SecurityContextHolder.getContext().getAuthentication().getName());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * function which check the solution of programming exercise
     * @param solution - programming solution to check
     * @return
     */
    @PostMapping("/programming/test")
    public Pair<String,Integer> checkSolutionWithTests(@RequestBody LongSolutionDto solution){
        return exerciseService.checkSolutionWithTests( longSolutionMapper.createLongSolution(solution));
    }
    /**
     * function which check the solution of programming exercise
     * @param solution - programming solution to check
     * @return
     */
    @PostMapping("/programming/check")
    public int checkSolution(@RequestBody LongSolutionDto solution){
       return exerciseService.checkSolution(longSolutionMapper.createLongSolution(solution));
    }
    /**
     * function which check the solution of abc exercise
     * @param solution - programming solution to check
     * @return
     */
    @PostMapping("/abc/check")
    public int checkSolution(@RequestBody ShortSolution solution){

        return exerciseService.checkSolution(solution);
    }


}
