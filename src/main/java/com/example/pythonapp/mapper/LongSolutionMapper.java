package com.example.pythonapp.mapper;
import com.example.pythonapp.dto.LongSolutionDto;
import com.example.pythonapp.model.LongSolution;

public class LongSolutionMapper {
    public LongSolution createLongSolution(LongSolutionDto longSolutionDto)
    {
        LongSolution longSolution = new LongSolution();
        longSolution.setSolutionContent(longSolutionDto.getSolutionContent());
        longSolution.setId(longSolutionDto.getId());
        longSolution.setExercise(longSolutionDto.getExercise());
        longSolution.setStudent(longSolutionDto.getStudent());
        longSolution.setScore(longSolutionDto.getScore());
        
        return longSolution;
    }
}
