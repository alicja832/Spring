package com.example.pythonapp.repository;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {

   Optional<Student> findById(long id);
   Optional<Student> findByEmail(String email);
   Optional<Student> findByName(String name);
   @Transactional
   @Modifying
   @Query(value = "update student set score=score+:score where id=:id", nativeQuery = true)
   void updateById(@Param("id") long id, @Param("score") int score);

}
