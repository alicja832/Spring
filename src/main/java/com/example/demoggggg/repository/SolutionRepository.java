package com.example.demoggggg.repository;

import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SolutionRepository extends JpaRepository<Solution,Long> {
    List<Solution> findAllByStudentEmailEquals(String StudentEmail);
    Solution findById(int id);
    Long deleteById(int id);
    @Transactional
    @Modifying
    @Query(value = "update solution set solutioncontent=:solutioncontent,score=:score where id=:id", nativeQuery = true)
    void updateById(@Param("id") int id, @Param("solutioncontent") String solutioncontent, @Param("score") int score);

}
