package com.example.pythonapp.service;

import com.example.pythonapp.exception.UserNotFoundException;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;
    @Override
    public Student save(Student student)
    {
        return studentRepository.save(student);
    }
    @Override
    public Student findbyId(long id)
    {
        return studentRepository.findById(id).orElseThrow(UserNotFoundException::new);
    }
    @Override
    public void update(long id,int score)
    {
        studentRepository.updateById(id,score);
    }
    @Override
    public List<Student> listStudents() {return studentRepository.findAll();}
    @Override
    public Student findByEmail(String email)
    {
        return studentRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);
    }
    @Override
    public Optional<Student> findByName(String name)
    {
        return studentRepository.findByName(name);
    }
}
