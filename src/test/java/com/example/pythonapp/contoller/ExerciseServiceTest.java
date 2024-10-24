package com.example.pythonapp.contoller;

import com.example.pythonapp.controller.ExerciseController;
import com.example.pythonapp.model.Exercise;
import javafx.util.Pair;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;

@SpringBootTest()
public class ExerciseServiceTest {
    @Autowired
    ExerciseController exerciseservice;
    @Test
    void exerciseControllerTest() {

    
        Assertions.assertEquals( "a\n",exerciseservice.getresponse("print('a')"));
        Assertions.assertEquals( "[-1, -1, 0, 4, 5, 6, 10, 14]\n",exerciseservice.getresponse("A = [-1,0,10,-1,14,4,6,5]\nn=len(A)\nfor i in range(n):\n\tmin=i\n\tfor j in range(i,n):\n\t\tif(A[j]<A[min]):\n\t\t\tmin = j\n\ttmp=A[i]\n\tA[i]=A[min]\n\tA[min]=tmp\nprint(A)"));
        Assertions.assertEquals( 1,exerciseservice.listExercises().size());
        
    
    }
    
 
}
