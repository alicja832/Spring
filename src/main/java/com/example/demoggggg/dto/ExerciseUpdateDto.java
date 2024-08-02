package com.example.demoggggg.dto;

import com.example.demoggggg.model.Solution;
import com.example.demoggggg.model.Teacher;

import java.util.Collection;

public class ExerciseUpdateDto {


        private long id;
        private String name;

        private String introduction;

        private String content;

        private int maxPoints;

        private String correctSolution;

        private String correctOutput = null;


        private Teacher teacher;

        public String getCorrectOutput() {
            return correctOutput;
        }

        public void setCorrectOutput(String correctOutput) {
            this.correctOutput = correctOutput;
        }
        public ExerciseUpdateDto() {
        }

        public String getIntroduction() {
            return introduction;
        }

        public void setIntroduction(String introduction) {
            this.introduction = introduction;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getMaxPoints() {
            return maxPoints;
        }

        public String getCorrectSolution() {
            return correctSolution;
        }

        public void setCorrectSolution(String correctSolution) {
            this.correctSolution = correctSolution;
        }

        public void setMaxPoints(int maxPoints) {
            this.maxPoints = maxPoints;
        }

        public  long getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

    public void setId(long id) {
        this.id = id;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public String getContent() { return content;}
        public void setContent(String content) {
            this.content = content;
        }


}
