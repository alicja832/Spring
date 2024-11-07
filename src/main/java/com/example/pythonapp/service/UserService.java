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
@Service
public interface UserService {

    Student saveStudent(Student student);
    Teacher saveTeacher(Teacher teacher);
    Teacher findById(long id);
    Solution saveSolution(Solution solution);
    Student findbyId(long id);
    UserEntity findByEmail(String email);
    Teacher findTeacherByEmail(String email);
    Student findStudentByEmail(String email);
    Teacher findTeacherByName(String name);
    Student findStudentByName(String name);
    public long loginStudent(Student student);
    public long loginTeacher(Teacher teacher);
    void updateStudent(long id,int score);
    void updateUser(String email,String password);
    long deleteUser(long id);
    List<Student> listStudents();
    List<UserEntity> listAll();
    UserEntity findUserByNameAndPassword(String username, String password);
    UserEntity findByName(String name);
}