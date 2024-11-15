package com.example.pythonapp.service;

import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;

import java.util.List;

public interface SolutionService {
    Solution save(Solution solution);
    List<Solution> findStudentSolution(Student student);
    Solution findById(int id);
    void update(int id,Solution solution);
    int deleteById(int id);
}
