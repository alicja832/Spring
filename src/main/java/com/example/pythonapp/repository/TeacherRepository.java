package com.example.pythonapp.repository;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher,Integer> {
    Teacher findByEmail(String email);
    Optional<Teacher> findByName(String name);
    Teacher findById(int id);
}