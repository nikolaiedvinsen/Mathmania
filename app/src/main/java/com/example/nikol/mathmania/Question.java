package com.example.nikol.mathmania;

public class Question {
    private final String mQuestion;
    private final int mSolution;

    public Question(String question, int solution) {
        mQuestion = question;
        mSolution = solution;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public int getAnswer() {
        return mSolution;
    }
}
