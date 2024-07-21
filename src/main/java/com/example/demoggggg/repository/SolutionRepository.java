package com.example.demoggggg.repository;

import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution,Integer> {
    List<Solution> findAllByStudentEquals(Student userId);
}
