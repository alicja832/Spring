package com.example.pythonapp.repository;
import com.example.pythonapp.model.ShortSolution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface ShortSolutionRepository extends JpaRepository<ShortSolution,Integer> {
    Optional<ShortSolution> findById(int id);
}
