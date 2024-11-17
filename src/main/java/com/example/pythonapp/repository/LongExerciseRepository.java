package com.example.pythonapp.repository;

import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.LongExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface LongExerciseRepository extends JpaRepository<LongExercise,Integer> {
    Optional<LongExercise> findByName(String name);
    Optional<LongExercise> findById(int id);
    @Transactional
    @Modifying
    @Query(value = "update long_exercise " +
            "set correct_solution=:correct_solution," +
            "correct_output=:correct_output where id=:id", nativeQuery = true)
    void updateById(@Param("id") long id,@Param("correct_solution") String correctSolution,@Param("correct_output") String correctOutput);
}
