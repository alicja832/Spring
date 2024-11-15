package com.example.pythonapp.service;

import com.example.pythonapp.exception.SolutionNotFoundException;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.repository.SolutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SolutionServiceImpl implements SolutionService {
    @Autowired
    private SolutionRepository solutionRepository;

    @Override
    public Solution save(Solution solution){
        return solutionRepository.save(solution);
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
    public void update(int id,Solution solution)
    {
        solutionRepository.updateById(id,solution.getSolutionContent(),solution.getScore());
    }

    @Override
    public int deleteById(int id)
    {
        solutionRepository.deleteById(id);
        return id;
    }
}
