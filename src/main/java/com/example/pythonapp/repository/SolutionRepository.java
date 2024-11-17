package com.example.pythonapp.repository;

import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolutionRepository extends JpaRepository<Solution,Integer> {
    List<Solution> findAllByStudentEquals(Student Student);
    Optional<Solution> findById(int id);
    Long deleteById(int id);
    List<Solution> getAllByExercise(Exercise Exercise);
}
