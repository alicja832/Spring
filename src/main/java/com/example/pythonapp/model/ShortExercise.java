package com.example.pythonapp.model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class ShortExercise extends Exercise{

    @Column(name="first_option")
    private String firstOption;
    @Column(name="second_option")
    private String secondOption;
    @Column(name="third_option")
    private String thirdOption;
    @Column(name="fourth_option")
    private String fourthOption;
    @Column(name="correct_answer")
    private char correctAnswer;

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
