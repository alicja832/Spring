package com.example.demoggggg.repository;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
   public Student findById(int id);
}
