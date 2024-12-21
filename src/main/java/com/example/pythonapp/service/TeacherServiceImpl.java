package com.example.pythonapp.service;

import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {
    @Autowired
    TeacherRepository teacherRepository;
    @Override
    public Teacher findById(int id)
    {
        return teacherRepository.findById(id);
    }

    @Override
    public Optional<Teacher> findByName(String name)
    {
        return teacherRepository.findByName(name);
    }

    @Override
    public Teacher save(Teacher teacher) {return teacherRepository.save(teacher);}

    @Override
    public void delete(int id){
        teacherRepository.deleteById(id);
    }
}
