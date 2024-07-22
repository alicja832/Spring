package com.example.demoggggg.service;
import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Student;
import com.example.demoggggg.model.Teacher;
import com.example.demoggggg.repository.SolutionRepository;
import com.example.demoggggg.repository.StudentRepository;
import com.example.demoggggg.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private SolutionRepository solutionRepository;

    @Override
    public Teacher saveTeacher(Teacher teacher) {
        return teacherRepository.save(teacher);
    }
    @Override
    public Student saveStudent(Student student)
    {
        return studentRepository.save(student);
    }

    /**
     *
     * TODO To będzie do zmiany ponieważ autoryzacja i uwierzytelnienie będzie innaczej
     * @param student
     * @return
     */
    @Override
    public int loginStudent(Student student)
    {
        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnorePaths("id")
                .withMatcher("model", ignoreCase());
        Student probe = new Student();
        probe.setEmail(student.getEmail());
        probe.setPassword(student.getPassword());
        System.out.println(student.getPassword());
        probe.setName(student.getName());
        Example<Student> example = Example.of(probe, modelMatcher);

        if(studentRepository.exists(example))
            return studentRepository.findOne(example).get().getId();
        return -1;
    }
    @Override
    public int loginTeacher(Teacher teacher)
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
    @Override
    public Solution saveSolution(Solution solution)
    {
        return solutionRepository.save(solution);
    }
    @Override
    public Student findbyId(int id)
    {
        return studentRepository.findById(id);
    }
    @Override
    public void updateStudent(int id,int score)
    {
        studentRepository.updateById(id,score);
    }
}

