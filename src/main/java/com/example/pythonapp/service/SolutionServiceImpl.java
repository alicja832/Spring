package com.example.pythonapp.service;

import com.example.pythonapp.exception.SolutionNotFoundException;
import com.example.pythonapp.model.*;
import com.example.pythonapp.repository.LongCorrectSolutionPartRepository;
import com.example.pythonapp.repository.LongSolutionRepository;
import com.example.pythonapp.repository.ShortSolutionRepository;
import com.example.pythonapp.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SolutionServiceImpl implements SolutionService {
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private LongSolutionRepository longSolutionRepository;
    @Autowired
    private ShortSolutionRepository shortSolutionRepository;

    @Override
    public Solution save(ShortSolution solution){
        return shortSolutionRepository.save(solution);
    }
    @Override
    public Solution save(LongSolution solution){
        return longSolutionRepository.save(solution);
    }

    @Override
    public List<Solution> findStudentSolution(Student student)
    {
        return  solutionRepository.findAllByStudentEquals(student);
    }


    @Override
    public Solution findById(int id)
    {
        return solutionRepository.findById(id).orElseThrow(SolutionNotFoundException::new);
    }
    @Override
    public LongSolution findLongSolutionById(int id)
    {
        return longSolutionRepository.findById(id).orElseThrow(SolutionNotFoundException::new);
    }
    @Override
    public void updateSolution(int id, LongSolution solution)
    {
        solutionRepository.updateById(id,solution.getScore());
        longSolutionRepository.updateById(id,solution.getSolutionContent());
    }
    @Override
    public ShortSolution findShortSolutionById(int id)
    {
        return shortSolutionRepository.findById(id).orElseThrow(SolutionNotFoundException::new);
    }
    @Override
    public List<Solution> getAllSolutionsByExercise (Exercise exercise)
    {
        return solutionRepository.getAllByExercise(exercise);
    }

}
