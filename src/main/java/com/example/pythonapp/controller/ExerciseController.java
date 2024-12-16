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
    public ResponseEntity<String> add(@RequestBody LongExerciseDto exercise){

        Pair<LongExercise,ArrayList<LongCorrectSolutionPart>> longExerciseArrayListPair;
        LongExercise longExercise;
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Teacher teacher = teacherService.findByName(name).get();
        String[] testingData =  exercise.getTestData();
        Integer[] points =  exercise.getPoints();
        
        try{
             longExerciseArrayListPair = createLongExercisefromDto(exercise);
        }
        catch(Exception ex)
        {
             System.out.println(ex.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        longExerciseArrayListPair.getKey().setTeacher(teacher);
        try {

            longExercise = exerciseService.save(longExerciseArrayListPair.getKey());

        }catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
         System.out.println(longExercise.getCorrectSolution());
        for (int i=0;i<testingData.length;i++) {
            
            try{
                exerciseService.save(new TestingData(longExercise, points[i],testingData[i]));
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
                 return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }


        for(LongCorrectSolutionPart longCorrectSolutionPart:longExerciseArrayListPair.getValue())
        {
            longCorrectSolutionPart.setExercise(longExercise);
            try{
                exerciseService.save(longCorrectSolutionPart);
            }
            catch(Exception ex)
            {
                System.out.println(ex.getMessage());
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        int amount = correctSolutionParts.length;
        ArrayList<LongCorrectSolutionPart> parts=new ArrayList<>();
        String[] correctSolutions = new String[amount];
        for(int i=0;i<amount;i++)
        {
        
            String result = exerciseService.getOut("\n"+correctSolutionParts[i]+"\n");
            if(!result.isEmpty() && result.contains("Error") || result.contains("TraceBack"))
                throw new Exception("Wrong code");
            parts.add(new LongCorrectSolutionPart(i,correctSolutionParts[i]));
            correctSolution+=correctSolutionParts[i];
            correctSolution+="\n";
            correctSolutions[i] = exerciseService.getOut(correctSolutionParts[i]);
        }
        return new Pair<>(new LongExercise(exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints(),correctSolution,exercise.getSolutionSchema(),exercise.getAccess()),parts);
    }
    @GetMapping("/testdata/{id}")
    public List<TestingData> getTestingData(@PathVariable int id){

      return  exerciseService. findAllTestingDataByExerciseId(id);
    
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
      
        List<TestingData> testingdata = exerciseService.findAllTestingDataByExerciseId(exercise.getId());
        String[] testdata = exercise.getTestData();
        Integer[] testingPoints = exercise.getPoints();
        int i;
     
        for(i=0;i<testingdata.size();i++)
        {
            exerciseService.update(testingdata.get(i).getId(),new TestingData(longExerciseArrayListPair.getKey(),testingPoints[i],testdata[i]));
        }
        Exercise exercisefound =exerciseService.findExerciseById(exercise.getId()).get();
        while(i<testdata.length)
        {
            try{
            exerciseService.save(new TestingData(exercisefound, testingPoints[i],testdata[i]));
            }
            catch(Exception exception)
            {
                System.out.println(exception.getMessage());
            }
            ++i;
        }

        try{
            exerciseService.update(exercise.getId(), longExerciseArrayListPair.getKey());
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Edycja zadania nie powiodła się");
        }

       
        int previous_size = exerciseService.findAllLongCorrectSolutionByExerciseId(exercise.getId()).size();
        int diff = previous_size - exercise.getMaxPoints();

        for(LongCorrectSolutionPart longCorrectSolutionPart:longExerciseArrayListPair.getValue())
        {
            if(exerciseService.findAllLongCorrectSolutionByExerciseIdAndOrder(exercise.getId(),longCorrectSolutionPart.getOrder()).isEmpty())
            {
                longCorrectSolutionPart.setExercise(exerciseService.findExerciseById(exercise.getId()).get());
                try{
                    exerciseService.save(longCorrectSolutionPart);
                }
                catch(Exception ex)
                {
                    System.out.println(ex.getMessage());
                }
            }
            try{
                exerciseService.update(exercise.getId(),longCorrectSolutionPart);
            }catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
       
        if(diff>0)
        {
            int actual_size = exercise.getMaxPoints();
            while(actual_size<previous_size)
            {
               
                try {
                    exerciseService.deleteLongCorrectSolutionPart(exercise.getId(), actual_size);
                }catch(Exception exception)
                {
                    System.out.println(exception.getMessage());
                }
                ++actual_size;
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
        Boolean access = false;
       
          if(userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
                access = true;
          if(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).isPresent())
            solutions = solutionService.findStudentSolution(studentService.findByName(SecurityContextHolder.getContext().getAuthentication().getName()).get());
      

        for(ShortExercise e:list)
        {
            Predicate<Solution> function = (solution) ->solution.getExercise().equals(e);
            boolean flag = solutions.stream().anyMatch(function);
            System.out.println(e.getAccess());
            if(e.getAccess()==access || !(e.getAccess()))
            {
                if(flag)
                    listExercise.add(new Pair<ShortExercise,Boolean>(e,true));
                else
                    listExercise.add(new Pair<ShortExercise,Boolean>(e,false));
            }
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
                    listExercise.add(new Pair<LongExercise,Boolean>(e,true));
                else
                    listExercise.add(new Pair<LongExercise,Boolean>(e,false));
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
