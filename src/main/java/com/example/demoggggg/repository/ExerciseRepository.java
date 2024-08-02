package com.example.demoggggg.repository;

import com.example.demoggggg.model.Exercise;
import com.example.demoggggg.model.Student;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
    Exercise findById(long id);
    @Transactional
    @Modifying
    @Query(value = "update exercise set name=:name,introduction=:introduction,content=:content,maxpoints=:maxpoints," +
            "correctsolution=:correctsolution," +
            "correctoutput=:correctoutput where id=:id", nativeQuery = true)
    void updateById(@Param("id") long id,@Param("name") String name,@Param("introduction") String introduction,@Param("content") String content,@Param("maxpoints") int maxPoints,@Param("correctsolution") String correctSolution,@Param("correctoutput") String correctOutput);
}
