package com.example.demoggggg.repository;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student,Long> {

   Student findById(long id);
   Student findByEmail(String email);
   Student findByName(String name);
   List<Student> findAll();
   @Transactional
   @Modifying
   @Query(value = "update student set score=:score where id=:id", nativeQuery = true)
   void updateById(@Param("id") int id, @Param("score") int score);

}
