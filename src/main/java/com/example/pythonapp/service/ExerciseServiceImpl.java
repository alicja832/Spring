package com.example.pythonapp.service;
import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.repository.ExerciseRepository;
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
    private PythonInterpreter interpreter;


    /**
     * A method which have to initailize the python interpreter
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
    @Override
    public Exercise save(Exercise exercise){
        return exerciseRepository.save(exercise);
    }

    @Override
    public void update(long id,Exercise exercise)
    {
        exerciseRepository.updateById(id,exercise.getName(),exercise.getIntroduction(),exercise.getContent(),exercise.getMaxPoints(),exercise.getCorrectSolution(),exercise.getCorrectOutput());
    }
    @Override
    public List<Exercise> getAllExercises()
    {
        return exerciseRepository.findAll();
    }
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
    public Optional<Exercise> findExerciseById(long id)
    {
        return exerciseRepository.findById(id);
    }

    @Override
    public void delete(long id)
    {
        exerciseRepository.deleteById(id);
    }

    @Override
    public Optional<Exercise> findExerciseByName(String name)
    {
        return exerciseRepository.findByName(name);
    }



}
