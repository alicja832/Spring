package com.example.pythonapp.service;
import com.example.pythonapp.dto.LongExerciseDto;
import com.example.pythonapp.exception.ExerciseNotFoundException;
import com.example.pythonapp.model.*;
import com.example.pythonapp.repository.*;
import javafx.util.Pair;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.StringWriter;
import java.util.*;
import java.io.PrintWriter;

@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private ShortExerciseRepository shortExerciseRepository;
    @Autowired
    private LongExerciseRepository longExerciseRepository;
    @Autowired
    private TestingDataRepository testingDataRepository;
    private PythonInterpreter interpreter;
    @Autowired
    private LongCorrectSolutionPartRepository longCorrectSolutionPartRepository;

    /**
     * A method which have to initialize the python interpreter
     */
    private void initializePythonInterpreter()
    {
        if(interpreter == null)
        {
            interpreter = new PythonInterpreter();
            Properties props = new Properties();
            props.put("python.home","target/classes/jython-plugins-tmp/Lib");
            props.put("python.import.site","false");
            Properties preprops = System.getProperties();
            String sth[]={};
            PythonInterpreter.initialize(preprops, props,sth);
        }
    }

    /**
     * A method which save long exercise
     */
    @Override
    public LongExercise save(LongExercise longExercise){
        return longExerciseRepository.save(longExercise);
    }
    /**
     * A method which save short exercise
     */
    @Override
    public ShortExercise save(ShortExercise shortExercise){
        return shortExerciseRepository.save(shortExercise);
    }
    /**
     * A method which update exercise
     */
    @Override
    public void updateExercise(int id,Exercise exercise)
    {
        exerciseRepository.updateById(id,exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints());
    }
    @Override
    public void updateLongExercise(LongExerciseDto exercise) throws Exception {
        Pair<LongExercise, ArrayList<LongCorrectSolutionPart>> longExerciseArrayListPair;
        longExerciseArrayListPair = createLongExerciseFromDto(exercise);
        List<TestingData> testingdata = findAllTestingDataByExerciseId(exercise.getId());
        String[] testdata = exercise.getTestData();
        Integer[] testingPoints = exercise.getPoints();
        int i;
        for (i = 0; i < testingdata.size(); i++) {
            update(testingdata.get(i).getId(), new TestingData(longExerciseArrayListPair.getKey(), testingPoints[i], testdata[i]));
        }
        Exercise exercisefound = findExerciseById(exercise.getId()).get();
        while (i < testdata.length) {
            save(new TestingData(exercisefound, testingPoints[i], testdata[i]));
            ++i;
        }


        update(exercise.getId(), longExerciseArrayListPair.getKey());


        int previous_size = findAllLongCorrectSolutionByExerciseId(exercise.getId()).size();
        int diff = previous_size - exercise.getMaxPoints();

        for (LongCorrectSolutionPart longCorrectSolutionPart : longExerciseArrayListPair.getValue()) {
            if (findAllLongCorrectSolutionByExerciseIdAndOrder(exercise.getId(), longCorrectSolutionPart.getOrder()).isEmpty()) {
                longCorrectSolutionPart.setExercise(findExerciseById(exercise.getId()).get());

                save(longCorrectSolutionPart);

            }
            update(exercise.getId(), longCorrectSolutionPart);
        }

        if (diff > 0) {
            int actual_size = exercise.getMaxPoints();
            while (actual_size < previous_size) {

                try {
                    deleteLongCorrectSolutionPart(exercise.getId(), actual_size);
                } catch (Exception exception) {
                    System.out.println(exception.getMessage());
                }
                ++actual_size;
            }
        }
    }
    /**
     * A method which find exercise
     */
    @Override
    public Optional<LongExercise> findLongExerciseByName(String name)
    {
        return longExerciseRepository.findByName(name);
    }
    @Override
    public List<LongCorrectSolutionPart> findAllLongCorrectSolutionByExerciseIdAndOrder(int exerciseId,int orderId)
    {
        return longCorrectSolutionPartRepository.findAllByExerciseIdAndOrder(exerciseId,orderId);
    }

    /**
     * A method which update long exercise
     */
    @Override
    public void update(int id, LongExercise exercise)
    {
         updateExercise(id,(Exercise)exercise);
         longExerciseRepository.updateById(id,exercise.getCorrectSolution(),exercise.getSolutionSchema());
    }
    /**
     * A method which update short exercise
     */
    @Override
    public void update(int id, ShortExercise exercise)
    {
        updateExercise(id,(Exercise)exercise);
       shortExerciseRepository.updateById(id,exercise.getFirstOption(),exercise.getSecondOption(),exercise.getThirdOption(),exercise.getFourthOption(),exercise.getCorrectAnswer());
    }

    /**
     * find all by exercise_id
     * @param exerciseId
     * @return
     */
    @Override
    public List<LongCorrectSolutionPart> findAllLongCorrectSolutionByExerciseId(int exerciseId)
    {
        return longCorrectSolutionPartRepository.findAllByExerciseId(exerciseId);
    }
    /***
     * update correctSolutionPart
     * @param id
     * @param correctSolutionPart
     * @return
     */
    public void update(int id,LongCorrectSolutionPart correctSolutionPart)
    {
        longCorrectSolutionPartRepository.update(id,correctSolutionPart.getOrder() ,correctSolutionPart.getCorrectSolutionPart());
    }
    /**
     * save the long solution part in the database
     * @param correctSolutionPart instance of correectSolutionPart to be save
     * @return saved longCorrectSolutionPart
     */
    @Override
    public LongCorrectSolutionPart save(LongCorrectSolutionPart correctSolutionPart) {return longCorrectSolutionPartRepository.save(correctSolutionPart);}
    /**
     * A method which list short exercises
     */
    @Override
    public List<ShortExercise> getAllShortExercises()
    {
        return shortExerciseRepository.findAll();
    }
    /**
     * A method which list long exercises
     */
    @Override
    public List<LongExercise> getAllLongExercises()
    {
        return longExerciseRepository.findAll();
    }
   

    /**
     * Function which create LongExercise from LongExerciseDto and save the parts of the solutions in the list
     * this is because the exercise_id have to be set in each correct solution Part
     * @param exercise
     * @return
     * @throws Exception
     */
    @Override
    public Pair<LongExercise, ArrayList<LongCorrectSolutionPart>> createLongExerciseFromDto(LongExerciseDto exercise) throws Exception
    {
        String[] correctSolutionParts =  exercise.getCorrectSolutions();
        String correctSolution="";
        StringBuilder cS = new StringBuilder(correctSolution);
        int amount = correctSolutionParts.length;
        ArrayList<LongCorrectSolutionPart> parts=new ArrayList<>();
        String[] correctSolutions = new String[amount];

        for(int i=0;i<amount;i++)
        {

            String result = getOut(correctSolutionParts[i]);

            if(result.isEmpty() || result.contains("Error") || result.contains("TraceBack"))
                throw new Exception("Nieprawidłowy kod rozwiązania");

            parts.add(new LongCorrectSolutionPart(i,correctSolutionParts[i]));
            cS.append(correctSolutionParts[i]);
            cS.append("\n");
            correctSolutions[i] = getOut(correctSolutionParts[i]);
        }
        correctSolution = cS.toString();
        return new Pair<>(new LongExercise(exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints(),
                correctSolution,exercise.getSolutionSchema(),exercise.getAccess()),parts);
    }
    @Override
    public void addLongExercise(LongExerciseDto exercise,Teacher teacher)throws Exception {
        Pair<LongExercise, ArrayList<LongCorrectSolutionPart>> longExerciseArrayListPair;
        LongExercise longExercise;

        String[] testingData = exercise.getTestData();
        Integer[] points = exercise.getPoints();

        longExerciseArrayListPair = createLongExerciseFromDto(exercise);


        longExerciseArrayListPair.getKey().setTeacher(teacher);
        try {
            longExercise = save(longExerciseArrayListPair.getKey());

        } catch (Exception e) {
            throw new Exception("Błąd zapisu zadania");
        }

        for (int i = 0; i < testingData.length; i++) {

            try {
                save(new TestingData(longExercise, points[i], testingData[i]));
            } catch (Exception ex) {
                throw new Exception("Błąd zapisu danych testowych");
            }
        }
    }
    /**
     * A method which use Python Interpreter to get output from console
    */
    @Override
    public String getOut(String text){

        initializePythonInterpreter();
        if(text.charAt(0)=='\"' || text.charAt(text.length()-1)=='\n')
                text = text.substring(1,text.length()-1);

        StringWriter output = new StringWriter();
        interpreter.setOut(output);
        try {
            interpreter.exec("\n"+text+"\n");
        }
        catch(Exception e)
        {
            e.printStackTrace(new PrintWriter(output));
            String response = output.toString();
            //separete the info warning from the all exception send from PythonInterpreter class
            return response.substring(0,response.indexOf("at org"));
        }
        return output.toString();
    }

    @Override
    public Optional<Exercise> findExerciseById(int id)
    {
        return exerciseRepository.findById(id);
    }
    @Override
    public Optional<ShortExercise> findShortExerciseById(int id)
    {
        return shortExerciseRepository.findById(id);
    }
    @Override
    public Optional<LongExercise> findLongExerciseById(int id)
    {
        return longExerciseRepository.findById(id);
    }

    @Override
    public void delete(int id)
    {
        try{
            if(!testingDataRepository.findAllByExerciseId(id).isEmpty())
                testingDataRepository.deleteAllByExerciseId(id);
            longCorrectSolutionPartRepository.deleteAllByExerciseId(id);
            
        }
        catch(Exception exception)
        {
            System.out.println(exception.getMessage());
        }
        exerciseRepository.deleteById(id);
    }


    @Override
    public  List<Exercise> findAllByTeacher_Id(int teacherId)
    {
        return exerciseRepository.findAllByTeacher_Id(teacherId);
    }
    @Override
    public void deleteLongCorrectSolutionPart(int exerciseId, int orderId)
    {
        longCorrectSolutionPartRepository.deleteAllByExerciseIdAndOrder(exerciseId,orderId);
    }
    @Override
    public TestingData save(TestingData testingData)
    {
        return testingDataRepository.save(testingData);
    }

    @Override
    public void update(int id,TestingData testingData)
    {
        testingDataRepository.update(id,testingData.getTestingData(),testingData.getPoints());
    }
   
    @Override
    public List<TestingData> findAllTestingDataByExerciseId(int exerciseId)
    {
        return testingDataRepository.findAllByExerciseId(exerciseId);
    }

    /**
     * @param function
     * @param parameters
     * @return
     */
    @Override
    public String runFunction(String function, String parameters)
    {
        int def = function.indexOf("def");
        int closing_tag = function.indexOf("(");
        String function_call = function.substring(def+4,closing_tag+1);
        String returned = getOut(function+"\n"+"print("+function_call+parameters+"))");
        if(returned.isEmpty() || returned.contains("None"))
        {
            returned = getOut(function+"\n"+function_call+parameters+")");
        }

        return returned;
    }

    @Override
    public Pair<String, Integer> checkSolutionWithTests(LongSolution longSolution) {

        int score = 0;
        String response = "";
        StringBuilder sr = new StringBuilder(response);
        String tmp;
        LongExercise exercise =  findLongExerciseByName(longSolution.getExercise().getName()).orElseThrow(ExerciseNotFoundException::new);
        List<TestingData> test =  findAllTestingDataByExerciseId(longSolution.getExercise().getId());

        for(int i=0;i<test.size();i++)
        {
            TestingData forTest = test.get(i);
            String solutionTest = runFunction(longSolution.getSolutionContent(),forTest.getTestingData());
            tmp = "Test "+i+"\nDane:"+forTest.getTestingData()+"\n";
            sr.append(tmp);
            tmp = "Oczekiwany rezultat: \n"+runFunction(exercise.getCorrectSolution(),forTest.getTestingData())+"\n";
            sr.append(tmp);
            tmp = "Oczekiwany rezultat: \n"+runFunction(exercise.getCorrectSolution(),forTest.getTestingData())+"\n";
            sr.append(tmp);
            sr.append(solutionTest);
            if(runFunction(exercise.getCorrectSolution(), forTest.getTestingData()).equals(solutionTest))
                score += forTest.getPoints();
        }

        return new Pair<>(sr.toString(),score);
    }

    @Override
    public int checkSolution(LongSolution longSolution) {

        LongExercise exercise =  findLongExerciseByName(longSolution.getExercise().getName()).orElseThrow(ExerciseNotFoundException::new);
        String output = getOut(longSolution.getSolutionContent());
        List<LongCorrectSolutionPart> parts = findAllLongCorrectSolutionByExerciseId(exercise.getId());
        int score = 0;
        for(LongCorrectSolutionPart longCorrectSolutionPart:parts)
        {
            if(output.contains(getOut(longCorrectSolutionPart.getCorrectSolutionPart())))
                ++score;
        }
        longSolution.setScore(score);
        return score;
    }

    @Override
    public int checkSolution(ShortSolution shortSolution) {
        ShortExercise exercise = findShortExerciseById(shortSolution.getId()).get();
        char correctAnswer = exercise.getCorrectAnswer();
        int maxPoints = shortSolution.getExercise().getMaxPoints();
        if (correctAnswer == Character.toUpperCase(shortSolution.getAnswer())) {
            shortSolution.setScore(maxPoints);
            return maxPoints;
        }
        return 0;
    }


}
