package com.example.demoggggg.service;

import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.repository.ExerciseRepository;
import com.example.demoggggg.repository.SolutionRepository;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.security.interfaces.EdECKey;
import java.util.ArrayList;
import java.util.List;


@Service
public class ExerciseServiceImpl implements ExerciseService {

    @Autowired
    private ExerciseRepository exerciseRepository;
    private PythonInterpreter interpreter;
    @Autowired
    private SolutionRepository solutionRepository;

    @Override
    public Exercise save(Exercise exercise){
        return exerciseRepository.save(exercise);
    }
    public void update(int id,Exercise exercise)
    {
         exerciseRepository.updateById(id,exercise.getName());
    }
    @Override
    public List<Exercise> getAllExercises()
    {
        return exerciseRepository.findAll();
    }
    @Override
    public String getOut(String text) {

        StringWriter output = new StringWriter();
        int i=0;
        int begin;
        String def = "def";
        String clas = "class";
        text = text.substring(1,text.length()-1);
        System.out.println(text);
        if(text.charAt(0)=='\n'){
            text = text.substring(1);
        }
        try {
            interpreter = new PythonInterpreter();
            ArrayList<String> list = new ArrayList<>();
            while((i+5)<(text.length()-1) && (text.substring(i,i+4).trim().equals(def) || text.substring(i,i+5).trim().equals(clas)))
            {
                System.out.println("halo");
                begin = i;
                while((i+3)<(text.length()-1) && !text.substring(i,i+3).contains("\\n")){
                    ++i;
                    System.out.println(text.substring(i,i+3));
                    System.out.println(" Pirnt");
                }
                i+=3;
                System.out.println(text.charAt(i)+"tu pomylka");
                while( (i+3)<(text.length()-1) && text.substring(i,i+3).contains("\\t")) {

                    System.out.println("halo2");
                    while ( (i+3)<(text.length()-1) &&  !text.substring(i,i+3).contains("\\n")) {
                        ++i;
                    }
                    i+=3;
                    if (i>=(text.length())) {
                        break;
                    }
                }
                if(i<=(text.length()))
                    list.add(text.substring(begin,i));
            }
            if(i<=(text.length()))
                list.add(text.substring(i));

            for(i=0;i< list.size()-1;i++)
            {
                System.out.println(list.get(i));
                interpreter.exec(list.get(i));
            }
            System.out.println(list.get(i));
            interpreter.setOut(output);
            interpreter.exec(list.get(i));
        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing Python code: " + e.getMessage();
        } finally {
            interpreter.close();
        }
        String result = output.toString();
        return result;
    }

    @Override
    public Exercise findById(int id)
    {
        return exerciseRepository.findById(id);
    }

    @Override
    public void delete(int id)
    {
        exerciseRepository.deleteById(id);
    }

    @Override
    public List<Solution> findStudentSolution(Student student)
    {
        return solutionRepository.findAllByStudentEquals(student);
    }

}
