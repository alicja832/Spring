package com.example.pythonapp.repository;
import com.example.pythonapp.model.LongCorrectSolutionPart;
import com.example.pythonapp.model.LongCorrectSolutionPartId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface LongCorrectSolutionPartRepository extends JpaRepository<LongCorrectSolutionPart, LongCorrectSolutionPartId> {
    @Transactional
    @Modifying
    @Query(value = "update solution_part " +
            "set correct_solution_part=:correct_solution_part, points = :points " +
            "where exercise_id=:exercise_id and order_id =:order_id", nativeQuery = true)
    void update(@Param("exercise_id") int Id,@Param("order_id") int orderId,@Param("correct_solution_part")  String correct_solution_part,@Param("points") Integer points);

    List<LongCorrectSolutionPart> findAllByExerciseId(int exerciseId);
    List<LongCorrectSolutionPart> findAllByExerciseIdAndOrder(int exerciseId, int order);
    @Transactional
    @Modifying
    void deleteAllByExerciseId(int exerciseId);
    @Transactional
    @Modifying
    void deleteAllByExerciseIdAndOrder(int exerciseId, int order);
}
