package com.example.pythonapp.service;

import com.example.pythonapp.exception.SolutionNotFoundException;
import com.example.pythonapp.model.*;
import com.example.pythonapp.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SolutionServiceImpl implements SolutionService {

    @Autowired
    private SolutionRepository solutionRepository;
    @Autowired
    private LongSolutionRepository longSolutionRepository;
    @Autowired
    private ShortSolutionRepository shortSolutionRepository;
    @Autowired
    private ExerciseService exerciseService;
    @Autowired
    private StudentService studentService;
  

    @Override
    public ShortSolution save(ShortSolution solution){
        return shortSolutionRepository.save(solution);
    }
    @Override
    public LongSolution save(LongSolution solution){
        return longSolutionRepository.save(solution);
    }

    @Override
    public List<Solution> findStudentSolution(Student student)
    {
        return  solutionRepository.findAllByStudentEquals(student);
    }


    @Override
    public Solution findById(int id)
    {
        return solutionRepository.findById(id).orElseThrow(SolutionNotFoundException::new);
    }
    @Override
    public LongSolution findLongSolutionById(int id)
    {
        return longSolutionRepository.findById(id).orElseThrow(SolutionNotFoundException::new);
    }
    @Override
    public void updateSolution(int id, LongSolution solution)
    {
        solutionRepository.updateById(id,solution.getScore());
        longSolutionRepository.updateById(id,solution.getSolutionContent());
    }
    @Override
    public ShortSolution findShortSolutionById(int id)
    {
        return shortSolutionRepository.findById(id).orElseThrow(SolutionNotFoundException::new);
    }
    @Override
    public List<Solution> getAllSolutionsByExercise (Exercise exercise)
    {
        return solutionRepository.getAllByExercise(exercise);
    }
    @Override
    public void delete(int id)
    {
        solutionRepository.deleteById(id);
    }

    /**
     *
     * @param solution - solution to save
     * @param studentName - student name who solution have to be save
     */
    @Override
    public void updateSolution(LongSolution solution,String studentName)
    {
        Student student = studentService.findByName(studentName).get();
        Exercise exercise =  exerciseService.findExerciseById(solution.getExercise().getId()).get();
        solution.setExercise(exercise);
        int newscore=solution.getScore();

        if(solution.getScore()==0)
        {
            newscore = exerciseService.checkSolution(solution);
        }

        solution.setStudent(student);
        solution.setScore(newscore);

        int oldScore = findById(solution.getId()).getScore();
        updateSolution(solution.getId(), solution);
        studentService.update(student.getId(),newscore-oldScore);
    }

    /**
     *
     * @param teacher - teacher who exercises have to be founded
     * @return list of maps with only chosen information about exercise
     */
    @Override
    public List<Map<String,String>> getAllTeacherExercises(Teacher teacher)
    {
        List<Exercise> exercises = new ArrayList<>();
        try{

            exercises = exerciseService.findAllByTeacher_Id(teacher.getId());

        }catch(Exception exception)
        {
            System.out.println(exception.getMessage());
        }
        List<Map<String, String>> exercisesWithInfo = new ArrayList<>();

        for (Exercise exercise : exercises) {

            Map<Integer, Integer> withScores = new HashMap<>();
            List<Solution> listSolutions = getAllSolutionsByExercise(exercise);
            for (Solution solution : listSolutions) {
                int score = solution.getScore();
                if (!withScores.containsKey(score))
                    withScores.put(score, 1);
                else
                    withScores.replace(score, withScores.get(score) + 1);
            }
            int mostpopularScore = 0;
            int scorecount = 0;
            Iterator<Map.Entry<Integer, Integer>> it = withScores.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<Integer, Integer> element = it.next();
                if (element.getValue() > scorecount) {
                    mostpopularScore = element.getKey();
                    scorecount = element.getValue();
                }
            }

            Map<String,String> info = new HashMap<>();
            info.put("id",Integer.toString(exercise.getId()));
            info.put("name",exercise.getName());
            info.put("content",exercise.getContent());
            info.put("introduction",exercise.getIntroduction());
            info.put("maxPoints",Integer.toString(exercise.getMaxPoints()));
            info.put("score",Integer.toString(mostpopularScore));
            info.put("quantity",Integer.toString(scorecount));
            info.put("access",Boolean.toString(exercise.getAccess()));
            if(exercise instanceof LongExercise)
            {
                info.put("correctSolution",((LongExercise)exercise).getCorrectSolution());
                info.put("solutionSchema",((LongExercise)exercise).getSolutionSchema());
            }

            if(exercise instanceof ShortExercise)
            {
                info.put("firstOption",((ShortExercise)exercise).getFirstOption());
                info.put("secondOption",((ShortExercise)exercise).getSecondOption());
                info.put("thirdOption",((ShortExercise)exercise).getThirdOption());
                info.put("fourthOption",((ShortExercise)exercise).getFourthOption());
                info.put("correctAnswer",Character.toString(((ShortExercise)exercise).getCorrectAnswer()));
            }

            exercisesWithInfo.add(info);
        }

        return exercisesWithInfo;
    }
}
