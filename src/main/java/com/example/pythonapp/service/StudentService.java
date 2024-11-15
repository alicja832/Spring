package com.example.pythonapp.service;

import com.example.pythonapp.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    Student save(Student student);
    Student findbyId(long id);
    Student findByEmail(String email);
    void update(long id,int score);
    List<Student> listStudents();
    Optional<Student> findByName(String name);
}
