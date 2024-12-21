package com.example.pythonapp.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ShortExercise extends Exercise{

    @Column(name="first_option",nullable=false)
    private String firstOption;
    @Column(name="second_option",nullable=false)
    private String secondOption;
    @Column(name="third_option",nullable=false)
    private String thirdOption;
    @Column(name="fourth_option")
    private String fourthOption;
    @Column(name="correct_answer",nullable=false)
    private char correctAnswer;

    public ShortExercise(String name, String introduction, String content, int maxPoints,boolean access,String firstOption,String secondOption,String thirdOption,String fourthOption,char correctAnswer) {
        super(name,introduction,content,maxPoints,access);
       this.firstOption = firstOption;
       this.secondOption = secondOption;
       this.fourthOption = fourthOption;
       this.thirdOption = thirdOption;
       this.correctAnswer = correctAnswer;
    }
    public ShortExercise() {}
    public String getFirstOption() {
        return firstOption;
    }

    public void setFirstOption(String firstOption) {
        this.firstOption = firstOption;
    }

    public String getSecondOption() {
        return secondOption;
    }

    public void setSecondOption(String secondOption) {
        this.secondOption = secondOption;
    }

    public String getThirdOption() {
        return thirdOption;
    }

    public void setThirdOption(String thirdOption) {
        this.thirdOption = thirdOption;
    }

    public String getFourthOption() {
        return fourthOption;
    }

    public void setFourthOption(String fourthOption) {
        this.fourthOption = fourthOption;
    }

    public char getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(char correctAnswer) {
        this.correctAnswer = correctAnswer;
    }
}
