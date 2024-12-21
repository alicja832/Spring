package com.example.pythonapp.service;

import com.example.pythonapp.model.Teacher;

import java.util.Optional;

public interface TeacherService {

    Teacher save(Teacher teacher);
    Teacher findById(int id);
    Optional<Teacher> findByName(String name);
    void delete(int id);
}
