package com.example.pythonapp.repository;

import com.example.pythonapp.model.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,Integer> {
    Optional<Exercise> findByName(String name);
    Optional<Exercise> findById(int id);
    List<Exercise> findAllByTeacher_Id(int teacherId);
    @Transactional
    @Modifying
    @Query(value = "update exercise set name=:name,introduction=:introduction,content=:content,max_points=:max_points, access=:access " +
           "where id=:id", nativeQuery = true)
    void updateById(@Param("id") int id,@Param("name") String name,@Param("introduction") String introduction,@Param("content") String content,@Param("max_points") int maxPoints,@Param("access") boolean access);
    @Transactional
    @Modifying
    void deleteById(int id);
}
