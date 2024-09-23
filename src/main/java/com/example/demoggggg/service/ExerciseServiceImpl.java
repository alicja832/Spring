package com.example.demoggggg.service;

import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.UserEntity;
import com.example.demoggggg.repository.ExerciseRepository;
import com.example.demoggggg.repository.SolutionRepository;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.security.interfaces.EdECKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    @Override
    public Solution save(Solution solution){

        if(solution.getOutput()==null)
        {
            solution.setOutput(getOut(solution.getSolutionContent()));
        }
        return solutionRepository.save(solution);
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
    public String getOut(String text) {

        StringWriter output = new StringWriter();
        interpreter = new PythonInterpreter();

        text = text.substring(1,text.length()-1);
        interpreter.setOut(output);

        try {
            interpreter.exec(text);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return output.toString();
    }

    @Override
    public Exercise findExerciseById(long id)
    {
        return exerciseRepository.findById(id);
    }

    @Override
    public void delete(long id)
    {
        exerciseRepository.deleteById(id);
    }

    @Override
    public List<Solution> findStudentSolution(Student student)
    {
        return  solutionRepository.findAllByStudentEmailEquals(student.getEmail());

    }

    @Override
    public Solution findSolutionById(int id)
    {
        return solutionRepository.findById(id);
    }

    @Override
    public void updateSolution(int id,Solution solution)
    {
        solutionRepository.updateById(id,solution.getSolutionContent(),solution.getScore());
    }

    @Override
    public long deleteSolutionById(long id)
    {
        solutionRepository.deleteById(id);
        return id;
    }



}