package com.example.demoggggg.service;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import com.example.demoggggg.model.UserEntity;
import com.example.demoggggg.repository.SolutionRepository;
import com.example.demoggggg.repository.StudentRepository;
import com.example.demoggggg.repository.TeacherRepository;
import com.example.demoggggg.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    @Override
    public Student saveStudent(Student student)
    {
        return studentRepository.save(student);
    }

    @Override
    public Solution saveSolution(Solution solution)
    {
        return solutionRepository.save(solution);
    }
    @Override
    public Student findbyId(long id)
    {
        return studentRepository.findById(id);
    }
    @Override
    public void updateStudent(int id,int score)
    {
        studentRepository.updateById(id,score);
    }
    @Override
    public UserEntity findbyEmail(String email)
    {
        return userRepository.findByEmail(email);
    }
    @Override
    public Teacher findById(long id)
    {
        return teacherRepository.findById(id);
    }
    @Override
    public Teacher findTeacherByEmail(String email)
    {
        return teacherRepository.findByEmail(email);
    }
    @Override
    public Student findStudentByEmail(String email)
    {
        return studentRepository.findByEmail(email);
    }
    @Override
    public UserEntity findUserByNameAndPassword(String name, String password)
    {
        return userRepository.findByNameAndPassword(name,password);
    }
    @Override
    public Teacher findTeacherByName(String name)
    {
        return teacherRepository.findByName(name);
    }
    @Override
    public Student findStudentByName(String name)
    {
        return studentRepository.findByName(name);
    }
    @Override
    public long deleteUser(long id)
    {
        userRepository.deleteById(id);
        return id;
    }
    @Override
    public List<UserEntity> listAll()
    {
        return userRepository.findAll();
    }
    @Override
    public UserEntity findByName(String name)
    {
        return userRepository.findByName(name);
    }

}

