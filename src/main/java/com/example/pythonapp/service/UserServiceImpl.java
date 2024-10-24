package com.example.pythonapp.service;
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

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    TeacherRepository teacherRepository;
    @Autowired
    SolutionRepository solutionRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    public Teacher saveTeacher(Teacher teacher) {return teacherRepository.save(teacher);}
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
    public List<Student> listStudents() {return studentRepository.findAll();}
    @Override
    public Student findStudentByEmail(String email)
    {
        return studentRepository.findByEmail(email);
    }
    @Override
    public UserEntity findUserByNameAndPassword(String name, String password) {return userRepository.findByNameAndPassword(name,password);}
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
    @Override
    public void updateUser(String email,String password)
    {
        userRepository.updateByEmail(email, password);
    }
    /**
     *
     * TODO To będzie do zmiany ponieważ autoryzacja i uwierzytelnienie będzie innaczej
     * @param student
     * @return
     */
    @Override
    public long loginStudent(Student student)
    {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("model", ignoreCase());
        Student probe = new Student();
        probe.setEmail(student.getEmail());
        probe.setPassword(student.getPassword());
        probe.setName(student.getName());
        Example<Student> example = Example.of(probe, modelMatcher);

        if(studentRepository.exists(example))
            return studentRepository.findOne(example).get().getId();
        return -1;
    }
    @Override
    public long loginTeacher(Teacher teacher)
    {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("model", ignoreCase());
        Teacher probe = new Teacher();
        probe.setEmail(teacher.getEmail());
        probe.setPassword(teacher.getPassword());
        probe.setName(teacher.getName());
        Example<Teacher> example = Example.of(probe, modelMatcher);

        if(teacherRepository.exists(example))
            return teacherRepository.findOne(example).get().getId();

        return -1;
    }
}