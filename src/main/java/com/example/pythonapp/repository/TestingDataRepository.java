package com.example.pythonapp.repository;
import com.example.pythonapp.model.TestingData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Repository
public interface TestingDataRepository extends JpaRepository<TestingData,Integer> {

    @Transactional
    @Modifying
    @Query(value = "update testing_data " +
            "set test_data=:test_data, " +
            "points=:points " +
            "where id=:id", nativeQuery = true)
    void update(@Param("id") int Id,@Param("test_data")  String testData,@Param("points")Integer points);

    List<TestingData> findAllByExerciseId(int exerciseId);

    @Transactional
    @Modifying
    void deleteAllByExerciseId(int exerciseId);
    
    

}
