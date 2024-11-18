package com.example.pythonapp.controller;
import com.example.pythonapp.dto.LongSolutionDto;
import com.example.pythonapp.exception.ExerciseNotFoundException;
import com.example.pythonapp.mapper.LongSolutionMapper;
import com.example.pythonapp.model.*;
import com.example.pythonapp.service.*;
import javafx.util.Pair;
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
     * add new programming exercise
     * @param exercise - new programming exercise to add
     * @return ResponseEntity with HTTP status
     */
    @PostMapping("/programming")
    public ResponseEntity<Exercise> add(@RequestBody LongExercise exercise){

        exercise.setCorrectOutput(exerciseService.getOut("\n"+exercise.getCorrectSolution()+"\n"));
        if(exercise.getCorrectOutput().contains("Error"))
            return new ResponseEntity<>(exercise, HttpStatus.BAD_REQUEST);
        if(exercise.getCorrectOutput().contains("TraceBack"))
            return new ResponseEntity<>(exercise, HttpStatus.BAD_REQUEST);

        Teacher teacher = setTeacherInExercise(exercise);
        try {
            exerciseService.save(exercise);
        }catch(Exception e){
            return new ResponseEntity<>(exercise, HttpStatus.BAD_REQUEST);
        }
        teacher.addExercise(exercise);
        return new ResponseEntity<>(exercise, HttpStatus.CREATED);
    }
    /**
     * add new abc exercise
     * @param exercise - new abc exercise to add
     * @return ResponseEntity with HTTP status
     */
    @PostMapping("/abc")
    public ResponseEntity<Exercise> add(@RequestBody ShortExercise exercise){

       Teacher teacher = setTeacherInExercise(exercise);
       exercise.setCorrectAnswer(Character.toUpperCase(exercise.getCorrectAnswer()));
       try {
            exercise = exerciseService.save(exercise);
        }catch(Exception e){
            return new ResponseEntity<>(exercise, HttpStatus.BAD_REQUEST);
        }
        teacher.addExercise(exercise);

        return new ResponseEntity<>(exercise, HttpStatus.CREATED);
    }
    private Teacher setTeacherInExercise(Exercise exercise)
    {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByName(name).get();
        exercise.setTeacher(teacher);
        return teacher;
    }
    /**
     * delete exercise
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Exercise> delete(@PathVariable int id){

	    Exercise exercise = exerciseService.findExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
        exerciseService.delete(id);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }
    /**
     * update short excercise
     * @param exercise - new version of exercise
     */
    @PutMapping("/abc")
    public ResponseEntity<String> update(@RequestBody ShortExercise exercise){
        
        try{
            ShortExercise exercisefound = exerciseService.findShortExerciseByName(exercise.getName()).get();
            exercise.setCorrectAnswer(Character.toUpperCase(exercise.getCorrectAnswer()));
            exerciseService.update(exercisefound.getId(), exercise);
        }catch(Exception e)
        {
           System.out.println(e.getMessage());
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edycja zadania nie powiodła się.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Edycja zadania powiodła się");
    }
    /**
     * update long excercise
     * @param exercise - new version of exercise
     */
    @PutMapping("/programming")
    public ResponseEntity<String> update(@RequestBody LongExercise exercise){
        
        try{
            LongExercise exercisefound = exerciseService.findLongExerciseByName(exercise.getName()).get();
            exercise.setCorrectOutput(exerciseService.getOut(exercisefound.getCorrectSolution()));
            exerciseService.update(exercisefound.getId(), exercise);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edycja zadania nie powiodła się");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Edycja zadania powiodła się");
    }
    /**
     * list all short exercises
     * @return list of pairs which exercies are done
     */
    @GetMapping("/abc")
    public List<Pair<ShortExercise,Boolean>> listShortExercises(){

        List<ShortExercise> list = exerciseService.getAllShortExercises();
        List<Pair<ShortExercise,Boolean>> listExercise = new ArrayList<>();
        List<Solution> solutions = new ArrayList<>();

        if(SecurityContextHolder.getContext().getAuthentication()!=null) {

            if(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
              solutions = solutionService.findStudentSolution(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        }

        for(ShortExercise e:list)
        {
            Predicate<Solution> function = (solution) ->solution.getExercise().equals(e);
            boolean flag = solutions.stream().anyMatch(function);
            if(flag)
                listExercise.add(new Pair<ShortExercise,Boolean>(e,true));
            else
                listExercise.add(new Pair<ShortExercise,Boolean>(e,false));
        }
        return listExercise;
    }
    /**
     * list all long exercises
     * @return list of pairs which exercies are done
     */
    @GetMapping("/programming")
    public List<Pair<LongExercise,Boolean>> listExercises(){

        List<LongExercise> list = exerciseService.getAllLongExercises();
        List<Pair<LongExercise,Boolean>> listExercise = new ArrayList<>();
        List<Solution> solutions = new ArrayList<>();

        if(SecurityContextHolder.getContext().getAuthentication()!=null) {

            if(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
                solutions = solutionService.findStudentSolution(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        }

        for(LongExercise e:list)
        {
            Predicate<Solution> function = (solution) ->solution.getExercise().equals(e);
            boolean flag = solutions.stream().anyMatch(function);
            if(flag)
                listExercise.add(new Pair<LongExercise,Boolean>(e,true));
            else
                listExercise.add(new Pair<LongExercise,Boolean>(e,false));
        }
        return listExercise;
    }
    /**
     * get one programming exercise
     * @param id
     * @return
     */
    @GetMapping("/one/programming/{id}")
    public List<LongExercise> FindLongExerciseById(@PathVariable int id) {

        LongExercise exercise = exerciseService.findLongExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
        return List.of(exercise);
    }
    /**
     * get one abc exercise
     * @param id
     * @return
     */
    @GetMapping("/one/abc/{id}")
    public List<ShortExercise> FindShortExerciseById(@PathVariable int id) {

        ShortExercise exercise = exerciseService.findShortExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
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
    @GetMapping("/solutions/programming")
    public List<Map<String,String>> getSolutions(){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Map<String, String>> exercisesAndScores = new ArrayList<>();
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
    @PostMapping("/solution/programming")
    public ResponseEntity<Solution> addSolution(@RequestBody LongSolutionDto solution){
        LongSolutionMapper longSolutionMapper=new LongSolutionMapper();
        Student loginStudent = studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if(solution.getScore()==0)
        {
            this.checkSolution(solution);
        }
        solution.setStudent(loginStudent);
        LongSolution solutionCreated = longSolutionMapper.createLongSolution(solution);
        solutionService.save(solutionCreated);
        studentService.update(loginStudent.getId(),loginStudent.getScore()+solution.getScore());
        return new ResponseEntity<>(solutionCreated,HttpStatus.CREATED);
    }
    /**
     * add one's abc solution with name exercise and score
     **/
    @PostMapping("/solution/abc")
    public ResponseEntity<Solution> addSolution(@RequestBody ShortSolution solution){

        Student loginStudent = studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get();
        if(solution.getScore()==0)
        {
            this.checkSolution(solution);
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
    public List<LongSolution> getSolution(@PathVariable int id){


        return List.of(solutionService.findLongSolutionById(id));
    }

    /**
     * function to update one solution by one user
     * @param solution new version of solution
     */
    @PutMapping("/solution")
    public void updateSolution(@RequestBody LongSolution solution){
        int oldScore = solutionService.findById(solution.getId()).getScore();
        solutionService.updateSolution(solution.getId(),solution);
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Student loginStudent = studentService.findByName(name).get();
        studentService.update(loginStudent.getId(),loginStudent.getScore()-oldScore+solution.getScore());
    }

    /**
     * function which check the solution of programming exercise
     * @param solution - programming solution to check
     * @return
     */
    @PostMapping("/programming/check")
    public int checkSolution(@RequestBody LongSolutionDto solution){

        LongExercise exercise =  exerciseService.findLongExerciseById(solution.getExercise().getId()).orElseThrow(ExerciseNotFoundException::new);
        String expectedOutput = exercise.getCorrectOutput();
        int maxPoints =  solution.getExercise().getMaxPoints();
        if(solution.getOutput() == null)
        {
            solution.setOutput(exerciseService.getOut(solution.getSolutionContent()));
        }
        if(expectedOutput.equals(solution.getOutput()) || expectedOutput.trim().equals(solution.getOutput().trim()))
        {
            solution.setScore(maxPoints);
            return maxPoints;
        }

        return 0;
    }
    /**
     * function which check the solution of abc exercise
     * @param solution - programming solution to check
     * @return
     */
    @PostMapping("/abc/check")
    public int checkSolution(@RequestBody ShortSolution solution){

        ShortExercise exercise = exerciseService.findShortExerciseById(solution.getExercise().getId()).orElseThrow(ExerciseNotFoundException::new);
        char correctAnswer = exercise.getCorrectAnswer();
        int maxPoints =  solution.getExercise().getMaxPoints();
        if(correctAnswer == Character.toUpperCase(solution.getAnswer()))
        {
            solution.setScore(maxPoints);
            return maxPoints;
        }

        return 0;
    }


}
