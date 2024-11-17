package com.example.pythonapp.repository;

import com.example.pythonapp.model.Exercise;
import com.example.pythonapp.model.LongExercise;
import com.example.pythonapp.model.ShortExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ShortExerciseRepository extends JpaRepository<ShortExercise,Integer> {
    Optional<ShortExercise> findByName(String name);
    Optional<ShortExercise> findById(int id);
    @Transactional
    @Modifying
    @Query(value = "update short_exercise "+
            "set first_option =:first_option," +
            "second_option =:second_option," +
            "third_option =:third_option," +
            "fourth_option =:fourth_option," +
            "correct_answer=:correct_answer where id=:id", nativeQuery = true)
    void updateById(@Param("id") int id, @Param("first_option") String firstOption, @Param("second_option") String secondOption,@Param("third_option") String thirdOption,@Param("fourth_option") String fourthOption,@Param("correct_answer") int answer);

}
