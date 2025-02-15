package com.example.pythonapp.service;
import com.example.pythonapp.model.Solution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.repository.SolutionRepository;
import com.example.pythonapp.repository.StudentRepository;
import com.example.pythonapp.repository.TeacherRepository;
import com.example.pythonapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import com.example.pythonapp.dto.VerificationRequest;

@Service
public interface UserService {
    Optional<UserEntity> findByEmail(String email);
    void updateUser(String email,String password);
    Optional<UserEntity> findByName(String name);
    void generateCode (String code)throws Exception;
    boolean codeVerify(VerificationRequest data);

}