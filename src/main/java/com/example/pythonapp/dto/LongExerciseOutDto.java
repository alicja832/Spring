package com.example.pythonapp.dto;

public class LongExerciseOutDto {

    private int id;
    private String name;
    private String introduction;
    private String content;
    private int maxPoints;
    private String correctOutput;


    public LongExerciseOutDto() {}
    public LongExerciseOutDto(int id,String name, String introduction, String content,int maxPoints,String correctOutput)
    {   
        this.id = id;
        this.name = name;
        this.introduction = introduction;
        this.content = content;
        this.maxPoints = maxPoints;
        this.correctOutput = correctOutput;

    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(int maxPoints) {
        this.maxPoints = maxPoints;
    }
    
    public String getCorrectOutput() {
        return correctOutput;
    }

    public void setCorrectOutput(String correctOutput) {
        this.correctOutput = correctOutput;
    }
   

}