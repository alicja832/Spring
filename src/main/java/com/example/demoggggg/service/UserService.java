package com.example.demoggggg.service;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;

import java.util.List;

public interface UserService {
    public Student saveStudent(Student student);
    public Teacher saveTeacher(Teacher teacher);
    public int loginStudent(Student student);
    public int loginTeacher(Teacher teacher);
    public Solution saveSolution(Solution solution);
    public Student findbyId(int id);

}

