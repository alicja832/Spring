package com.example.demoggggg.repository;
import com.example.demoggggg.model.Exercise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "classpath:application.properties")
@SpringBootTest
public class ExerciseRepositoryTest {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Test
    public void testFindAll() {
        Exercise exercise1 = new Exercise();
        exercise1.setContent("Idea of content1");
        exerciseRepository.save(exercise1);

        Exercise exercise2 = new Exercise();
        exercise2.setContent("Idea of content2");
        exerciseRepository.save(exercise2);

        List<Exercise> exercises = exerciseRepository.findAll();
        assertEquals(2, exercises.size());
        assertTrue(exercises.stream().anyMatch(exercise -> "Idea of content1".equals(exercise.getContent())));
        assertTrue(exercises.stream().anyMatch(exercise -> "Idea of content2".equals(exercise.getContent())));
        exerciseRepository.deleteAll();
    }
    @Test
    public void testAutoIncrementId() {
        Exercise exercise1 = new Exercise();
        exercise1.setContent("Idea of content1");
        exerciseRepository.save(exercise1);

        Exercise exercise2 = new Exercise();
        exercise2.setContent("Idea of content2");
        exerciseRepository.save(exercise2);

        List<Exercise> exercises = exerciseRepository.findAll();
        assertEquals(1, exercises.get(0).getId());
        assertEquals(2, exercises.get(1).getId());
        exerciseRepository.deleteAll();
    }
    @Test
    public void testDelete() {
        System.out.println(exerciseRepository.findAll().size());
        Exercise exercise1 = new Exercise();
        exercise1.setContent("Idea of content1");
        exerciseRepository.save(exercise1);

        Exercise exercise2 = new Exercise();
        exercise2.setContent("Idea of content2");
        exerciseRepository.save(exercise2);

        exerciseRepository.delete(exercise1);
        List<Exercise> exercises = exerciseRepository.findAll();
        assertEquals(1, exercises.size());
        assertFalse(exercises.stream().anyMatch(exercise -> "Idea of content1".equals(exercise.getContent())));
        assertTrue(exercises.stream().anyMatch(exercise -> "Idea of content2".equals(exercise.getContent())));
    }
}
