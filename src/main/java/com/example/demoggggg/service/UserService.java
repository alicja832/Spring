package com.example.demoggggg.service;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import com.example.demoggggg.model.UserEntity;

import java.util.List;

public interface UserService {

    Student saveStudent(Student student);
    Teacher saveTeacher(Teacher teacher);
    Teacher findById(long id);
    Solution saveSolution(Solution solution);
    Student findbyId(long id);
    UserEntity findbyEmail(String email);
    Teacher findTeacherByEmail(String email);
    Student findStudentByEmail(String email);
    Teacher findTeacherByName(String name);
    Student findStudentByName(String name);
    public long loginStudent(Student student);
    public long loginTeacher(Teacher teacher);
    void updateStudent(int id,int score);
    long deleteUser(long id);
    List<UserEntity> listAll();
    UserEntity findUserByNameAndPassword(String username, String password);
    UserEntity findByName(String name);
}