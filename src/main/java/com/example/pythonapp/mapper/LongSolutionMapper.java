package com.example.pythonapp.mapper;
import com.example.pythonapp.dto.UserCreationDto;
import com.example.pythonapp.dto.LongSolutionDto;
import com.example.pythonapp.model.LongSolution;
import com.example.pythonapp.model.Student;
import com.example.pythonapp.model.Teacher;
import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.model.enums.Role;

public class LongSolutionMapper {
    public LongSolution createLongSolution(LongSolutionDto longSolutionDto)
    {
        LongSolution longSolution = new LongSolution();
        longSolution.setSolutionContent(longSolutionDto.getSolutionContent());
        longSolution.setId(longSolutionDto.getId());
        longSolution.setExercise(longSolutionDto.getExercise());
        longSolution.setStudent(longSolution.getStudent());
        longSolution.setScore(longSolution.getScore());
        return longSolution;
    }
}
