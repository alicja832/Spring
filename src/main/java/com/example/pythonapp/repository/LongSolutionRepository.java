package com.example.pythonapp.repository;

import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.LongSolution;
import com.example.pythonapp.model.Solution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface LongSolutionRepository extends JpaRepository<LongSolution,Integer> {
    @Transactional
    @Modifying
    @Query(value = "update long_solution set solution_content=:solutioncontent,score=:score where id=:id", nativeQuery = true)
    void updateById(@Param("id") int id, @Param("solutioncontent") String solutioncontent, @Param("score") int score);

}
