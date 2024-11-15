package com.example.pythonapp.service;
import com.example.pythonapp.exception.UserNotFoundException;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.repository.SolutionRepository;
import com.example.pythonapp.repository.StudentRepository;
import com.example.pythonapp.repository.TeacherRepository;
import com.example.pythonapp.repository.UserRepository;
import org.hibernate.boot.archive.internal.StandardArchiveDescriptorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public Optional<UserEntity> findByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }
    @Override
    public int deleteUser(int id)
    {
        userRepository.deleteById(id);
        return id;
    }
    @Override
    public Optional<UserEntity> findByNameAndPassword(String name, String password)
    {
        return userRepository.findByNameAndPassword(name,password);
    }

    @Override
    public Optional<UserEntity> findByName(String name)
    {
        return userRepository.findByName(name);
    }
    @Override
    public void updateUser(String email,String password)
    {
        userRepository.updateByEmail(email, password);
    }
    

}