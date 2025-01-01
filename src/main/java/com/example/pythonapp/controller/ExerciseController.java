package com.example.pythonapp.controller;
import com.example.pythonapp.dto.LongExerciseOutDto;
import com.example.pythonapp.PythonApplication;
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import com.example.pythonapp.dto.LongExerciseDto;

@RestController
@CrossOrigin (origins=PythonApplication.frontend,maxAge = 3600)
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
    @Autowired
    private UserService userService;

    /**
     * add new programming exercise
     * @param exercise - new programming exercise to add
     * @return ResponseEntity with HTTP status
     */
    @PostMapping("/programming")
    public ResponseEntity<String> addLongExercise(@RequestBody LongExerciseDto exercise){

        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        if(teacherService.findByName(name).isEmpty()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Użytkownik nie jest zalogowanym nauczycielem.");
        Teacher teacher = teacherService.findByName(name).get();
        
        try{
            exerciseService.addLongExercise(exercise,teacher);
        }
        catch(Exception ex)
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }



    @GetMapping("/testdata/{id}")
    //@GetMapping("/testData/{id}")
    public List<TestingData> getTestingData(@PathVariable int id){

      return  exerciseService.findAllTestingDataByExerciseId(id);
    
    }
    /**
     * add new abc exercise
     * @param exercise - new abc exercise to add
     * @return ResponseEntity with HTTP status
     */
    @PostMapping("/abc")
    public ResponseEntity<String> addShortExercise(@RequestBody ShortExercise exercise){

       String name = SecurityContextHolder.getContext().getAuthentication().getName();
       Teacher teacher = teacherService.findByName(name).get();
       exercise.setTeacher(teacher);
       exercise.setCorrectAnswer(Character.toUpperCase(exercise.getCorrectAnswer()));
       try {
            exerciseService.save(exercise);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>( HttpStatus.CREATED);
    }

    /**
     * delete exercise
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Exercise> deleteExercise(@PathVariable int id){

	    Exercise exercise = exerciseService.findExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
        exerciseService.delete(id);
        return new ResponseEntity<>(exercise, HttpStatus.OK);
    }
    /**
     * update short excercise
     * @param exercise - new version of exercise
     */
    @PutMapping("/abc")
    public ResponseEntity<String> updateShortExercise(@RequestBody ShortExercise exercise){
        
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
    public ResponseEntity<String> updateLongExercise(@RequestBody LongExerciseDto exercise){
        
        try{
            System.out.println(exercise.getSolutionSchema());
            exerciseService.updateLongExercise(exercise);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
        boolean access = false;

        if(userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
            access = true;
        if(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
            solutions = solutionService.findStudentSolution(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get());
    
        for(ShortExercise e:list)
        {
            Predicate<Solution> function = (solution) ->solution.getExercise().equals(e);
            boolean flag = solutions.stream().anyMatch(function);
            if(e.getAccess() == access || !(e.getAccess()))
            {
                if(flag)
                    listExercise.add(new Pair<>(e,true));
                else
                    listExercise.add(new Pair<>(e,false));
            }
        }
        return listExercise;
    }
    
    /**
     * list all long exercises
     * @return list of pairs which exercies are done
     */
    @GetMapping("/programming")
    public List<Pair<LongExercise,Boolean>> listLongExercises(){

        List<LongExercise> list = exerciseService.getAllLongExercises();
        List<Pair<LongExercise,Boolean>> listExercise = new ArrayList<>();
        List<Solution> solutions = new ArrayList<>();
        Boolean access = false;
         
       
            if(userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
                access = true;
            if(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
                solutions = solutionService.findStudentSolution(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get());
            
       

        for(LongExercise e:list)
        {
           
            if(e.getAccess() == access || !(e.getAccess()))
            {
                Predicate<Solution> function = (solution) ->solution.getExercise().equals(e);
                boolean flag = solutions.stream().anyMatch(function);
                if(flag)
                    listExercise.add(new Pair<>(e,true));
                else
                    listExercise.add(new Pair<>(e,false));
            }
        }
        return listExercise;
    }
    /**
     * get one programming exercise
     * @param id
     * @return
     */
    @GetMapping("/one/programming/{id}")
    // @GetMapping("/oneProgramming/{id}")
    public List<LongExerciseOutDto> FindLongExerciseById(@PathVariable int id) {

        LongExercise exercise = exerciseService.findLongExerciseById(id).orElseThrow(ExerciseNotFoundException::new);
        if(!exercise.getSolutionSchema().isEmpty())
        {
            String dataToTest = null;
           
            try{
                List<TestingData> testData = exerciseService.findAllTestingDataByExerciseId(id);
                if(!testData.isEmpty())
                    dataToTest = testData.get(0).getTestingData();
                LongExerciseOutDto dto = new LongExerciseOutDto(id,exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints(),"Wynik dla danych: "+dataToTest+"\n"+exerciseService.runFunction(exercise.getCorrectSolution(),dataToTest));
                dto.setSolutionSchema(exercise.getSolutionSchema());
                return List.of(dto);
            }catch(Exception ex)
            {
                System.out.println(ex.getMessage());
            }
            

        }
        return List.of(new LongExerciseOutDto(id,exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints(),exercise.getCorrectSolution()));
    }
    /**
     * get one abc exercise
     * @param id
     * @return
     */
    @GetMapping("/one/abc/{id}")
    // @GetMapping("/oneAbc/{id}")
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
