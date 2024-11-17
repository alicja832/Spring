package com.example.pythonapp.service;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.LongExercise;
import com.example.pythonapp.model.ShortExercise;
import com.example.pythonapp.repository.ExerciseRepository;
import com.example.pythonapp.repository.LongExerciseRepository;
import com.example.pythonapp.repository.ShortExerciseRepository;
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
    private PythonInterpreter interpreter;


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
    /**
     * A method which update long exercise
     */
    @Override
    public void update(int id, LongExercise exercise)
    {
         updateExercise(id,(Exercise)exercise);
       longExerciseRepository.updateById(id,exercise.getCorrectSolution(),exercise.getCorrectOutput());
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
        exerciseRepository.deleteById(id);
    }

    @Override
    public Optional<Exercise> findExerciseByName(String name)
    {
        return exerciseRepository.findByName(name);
    }
}
