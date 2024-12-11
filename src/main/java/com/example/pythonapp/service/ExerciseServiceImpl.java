package com.example.pythonapp.service;
import com.example.pythonapp.model.*;
import com.example.pythonapp.repository.*;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
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
        Properties props = new Properties();
        props.put("python.home","target/classes/jython-plugins-tmp/Lib");
        props.put("python.import.site","false");
        Properties preprops = System.getProperties();
        String sth[]={};
        PythonInterpreter.initialize(preprops, props,sth);
        if(interpreter == null)
            interpreter = new PythonInterpreter();
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
    /**
     * method which find exercise
     */
    @Override
    public Optional<ShortExercise> findShortExerciseByName(String name)
    {
        return shortExerciseRepository.findByName(name);
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
    public LongCorrectSolutionPart save(LongCorrectSolutionPart correctSolutionPart)
    {
        return longCorrectSolutionPartRepository.save(correctSolutionPart);
    }
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
            interpreter.exec(text);
        }
        catch(Exception e)
        {
        
            e.printStackTrace(new PrintWriter(output));
            String pythonError = output.toString();
            int end = pythonError.indexOf("at");
            return pythonError.substring(0,end);
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
    public Optional<Exercise> findExerciseByName(String name)
    {
        return exerciseRepository.findByName(name);
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
    @Override
    public void deleteAllTestingDataByExerciseId(int exerciseId)
    {
       testingDataRepository.deleteAllByExerciseId(exerciseId);
    }
    @Override
    public String runFunction(String function, String parameters)
    {
        int funct = function.indexOf("def");
        int closing_tag = function.indexOf("(");
        String function_call = function.substring(funct+4,closing_tag+1);
        System.out.println("\n"+function+"\n"+"print("+function_call+parameters+"))"+"\n");
        return getOut("\n"+function+"\n"+"print("+function_call+parameters+"))"+"\n");
    }

}
