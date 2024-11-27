package com.example.pythonapp.controller;
import com.example.pythonapp.dto.LongExerciseOutDto;
import com.example.pythonapp.exception.ExerciseNotFoundException;
import com.example.pythonapp.model.*;
import com.example.pythonapp.dto.LongExerciseDto;
import com.example.pythonapp.service.*;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import com.example.pythonapp.dto.LongExerciseDto;

@RestController
//@CrossOrigin(origins = "https://pythonfront-dnb5a2epcyh3e0ep.polandcentral-01.azurewebsites.net", maxAge=360000)
@CrossOrigin (origins="http://localhost:3000",maxAge = 3600)
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
    public ResponseEntity<String> add(@RequestBody LongExerciseDto exercise){

        Pair<LongExercise,ArrayList<LongCorrectSolutionPart>> longExerciseArrayListPair;
        LongExercise longExercise;
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByName(name).get();
        
        try{
             longExerciseArrayListPair = createLongExercisefromDto(exercise);
        }
        catch(Exception exception)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        longExerciseArrayListPair.getKey().setTeacher(teacher);
        try {
            longExercise = exerciseService.save(longExerciseArrayListPair.getKey());
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        teacher.addExercise(longExercise);
        for(LongCorrectSolutionPart longCorrectSolutionPart:longExerciseArrayListPair.getValue())
        {
            
            longCorrectSolutionPart.setExercise(longExercise);
            try{
                exerciseService.save(longCorrectSolutionPart);
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Function which create LongExercise from LongExerciseDto and save the parts of the solutions in the list
     * this is because the exercise_id have to be set in each correct solution Part
     * @param exercise
     * @return
     * @throws Exception
     */
    Pair<LongExercise,ArrayList<LongCorrectSolutionPart>> createLongExercisefromDto(LongExerciseDto exercise) throws Exception
    {
        String[] correctSolutionParts =  exercise.getCorrectSolutions();
        String correctSolution="";
        int amount = exercise.getMaxPoints();
        ArrayList<LongCorrectSolutionPart> parts=new ArrayList<>();
        String[] correctSolutions = new String[amount];
        for(int i=0;i<amount;i++)
        {
            String result = exerciseService.getOut(correctSolutionParts[i]);
            if(result.contains("Error") || result.contains("TraceBack"))
                throw new Exception("Wrong code");
            parts.add(new LongCorrectSolutionPart(i,correctSolutionParts[i]));
            correctSolution+=correctSolutionParts[i];
            correctSolution+="\n";
            correctSolutions[i] = exerciseService.getOut(correctSolutionParts[i]);
        }
        return new Pair<>(new LongExercise(exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints(),correctSolution),parts);
    }
    /**
     * add new abc exercise
     * @param exercise - new abc exercise to add
     * @return ResponseEntity with HTTP status
     */
    @PostMapping("/abc")
    public ResponseEntity<String> add(@RequestBody ShortExercise exercise){

       String name = SecurityContextHolder.getContext().getAuthentication().getName();
       Teacher teacher = teacherService.findByName(name).get();
       exercise.setTeacher(teacher);
       exercise.setCorrectAnswer(Character.toUpperCase(exercise.getCorrectAnswer()));
       try {
            exercise = exerciseService.save(exercise);
         
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        Exercise addExercise = exercise; 
        teacher.addExercise(addExercise);
        return new ResponseEntity<>( HttpStatus.CREATED);
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
            exercise.setCorrectAnswer(Character.toUpperCase(exercise.getCorrectAnswer()));
            exerciseService.update(exercise.getId(), exercise);
        }catch(Exception e)
        {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edycja zadania nie powiodła się.");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Edycja zadania powiodła się");
    }
    /**
     * update long excercise
     * @param exercise - new version of exercise
     */
    @PutMapping("/programming")
    public ResponseEntity<String> update(@RequestBody LongExerciseDto exercise){

        Pair<LongExercise,ArrayList<LongCorrectSolutionPart>> longExerciseArrayListPair;
       
        try{
            longExerciseArrayListPair = createLongExercisefromDto(exercise);
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
     
        try{
            exerciseService.update(exercise.getId(), longExerciseArrayListPair.getKey());
        }
        catch(Exception e)
        {
             System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edycja zadania nie powiodła się");
        }
        System.out.println(longExerciseArrayListPair.getValue());
        for(LongCorrectSolutionPart longCorrectSolutionPart:longExerciseArrayListPair.getValue())
        {
            try{
                exerciseService.update(exercise.getId(),longCorrectSolutionPart);
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
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
    public List<LongExerciseOutDto> FindLongExerciseById(@PathVariable int id) {

        LongExercise exercise = exerciseService.findLongExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
        LongExerciseOutDto exerciseDto = new LongExerciseOutDto(id,exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints(), exerciseService.getOut("\n"+exercise.getCorrectSolution()+"\n"));
        return List.of(exerciseDto);
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
    @PostMapping("/out")
    public String getResponse(@RequestBody String text){
        return exerciseService.getOut("\n"+text+"\n");
    }
}
