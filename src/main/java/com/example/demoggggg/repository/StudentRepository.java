package com.example.demoggggg.repository;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
   public Student findById(int id);
   @Transactional
   @Modifying
   @Query(value = "update student set score=:score where id=:id", nativeQuery = true)
   void updateById(@Param("id") int id, @Param("score") int score);
}
