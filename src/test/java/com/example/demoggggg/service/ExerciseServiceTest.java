package com.example.demoggggg.service;
import com.example.demoggggg.repository.ExerciseRepository;
import com.example.demoggggg.model.Exercise;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ExerciseServiceTest {

    @Autowired
    private ExerciseService exerciseService;

    @MockBean
    private ExerciseRepository exerciseRepository;

    @Test
    public void testgetAllExercises() {
        // Konfiguracja mocka
        when(exerciseRepository.findAll()).thenReturn(new ArrayList<>());

        // Wywołanie metody serwisu
        List<Exercise> exercises = exerciseService.getAllExercises();

        // Asercje
        assertNotNull(exercises);
        Mockito.verify(exerciseRepository).findAll();
    }
    @Test
    public void deleteExercise() {

        // Konfiguracja mocka
        // Konfiguracja mocka, aby metoda save() akceptowała dowolny Exercise
        Exercise exercise = new Exercise();
        when(exerciseRepository.save(exercise)).thenReturn(exercise);
        Mockito.doAnswer(invocation -> {
            // Po zapisaniu, findAll powinno zwracać pustą listę
            when(exerciseRepository.findAll()).thenReturn(new ArrayList<>());
            return null;
        }).when(exerciseRepository).delete(Mockito.any(Exercise.class));

        exerciseService.save(exercise);
        exerciseService.delete(exercise.getId());
        // Wywołanie metody serwisu
        List<Exercise> exercises = exerciseService.getAllExercises();

        // Asercje
        assertTrue(exercises.isEmpty());

    }

}
