package com.example.nikol.mathmania;

import java.util.Collections;
import java.util.List;

class QuestionHandler {
    private List<Question> mQuestionList;
    private int mNextQuestionIndex;

    public QuestionHandler(List<Question> questionList) {
        mQuestionList = questionList;
        Collections.shuffle(mQuestionList);

        mNextQuestionIndex = 0;

    }

    public Question getNextQuestion() {
        if (mNextQuestionIndex == mQuestionList.size()) {
            mNextQuestionIndex = 0;
        }

        return mQuestionList.get(mNextQuestionIndex++);
    }

    public int getNextQuestionIndex() {
        return mNextQuestionIndex;
    }
}
