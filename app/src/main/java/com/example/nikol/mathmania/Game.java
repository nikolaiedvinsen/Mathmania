package com.example.nikol.mathmania;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Game extends AppCompatActivity implements View.OnClickListener  {

    private SharedPreferences preferences;

    private QuestionHandler mQuestionHandler;
    private Question mCurrentQuestion;

    private TextView mResultView;
    private TextView mQuestionView;
    private TextView mEqualsView;
    private TextView mAnswerView;
    private TextView mSolutionView;

    private TextView mNumOfCorrectView;
    private TextView mNumOfWrongView;
    private TextView mRoundView;
    private CountDownTimer countdown;

    private int mNumOfQuestions;
    private int mNumOfQuestionsLeft;
    private int mNumOfCorrect;
    private int mNumOfWrong;

    private boolean mEnableTouch;
    private Vibrator vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        TextView toolbarTitle = findViewById(R.id.text_toolbar_title);
        toolbarTitle.setText(R.string.game_title);

        mQuestionHandler = generateQuestionHandler(getResources().getStringArray(R.array.questions));

        mResultView = findViewById(R.id.result);
        mQuestionView = findViewById(R.id.question);
        mEqualsView = findViewById(R.id.equals);
        mAnswerView = findViewById(R.id.answer);
        mSolutionView = findViewById(R.id.solution);

        mNumOfCorrectView = findViewById(R.id.num_of_correct);
        mNumOfWrongView = findViewById(R.id.num_of_wrong);
        mRoundView = findViewById(R.id.round_view);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mNumOfQuestions = preferences.getInt("num_of_rounds", 5);
        mCurrentQuestion = mQuestionHandler.getNextQuestion();

        mEnableTouch = false;
        vb = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        String temp = "1/" + mNumOfQuestions;
        mRoundView.setText(temp);

        if (savedInstanceState != null){
            mQuestionView.setText(savedInstanceState.getString("mQuestionView"));
            mNumOfQuestionsLeft = savedInstanceState.getInt("mNumOfQuestionsLeft");
            mNumOfCorrect = savedInstanceState.getInt("mNumOfCorrect");
            mNumOfCorrectView.setText(String.valueOf(mNumOfCorrect));
            mNumOfWrong = savedInstanceState.getInt("mNumOfWrong");
            mNumOfWrongView.setText(Integer.toString(mNumOfWrong));
        }
        else {
            mNumOfQuestionsLeft = mNumOfQuestions;
            mNumOfCorrect = 0;
            mNumOfWrong = 0;
        }

        createNumpad();
        addAnswerListener();
        startCountdown();

    }

    private QuestionHandler generateQuestionHandler(String[] questionList) {
        List<Question> questionObjectList = new ArrayList<>();

        for (int i = 0; i < questionList.length; i++) {
            String[] splitQuestion = questionList[i].split(",");
            String question = splitQuestion[0];
            int solution = Integer.parseInt(splitQuestion[1]);
            questionObjectList.add(new Question(question, solution));

        }
        return new QuestionHandler(questionObjectList);
    }

    private void addAnswerListener() {
        mAnswerView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkAnswer();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void startCountdown() {
        countdown = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                int seconds = (int) millisUntilFinished / 1000;
                if (seconds == 0) {
                    mQuestionView.setText("Go!");
                }
                else {
                    mQuestionView.setText(String.format("%d", seconds));
                }
            }

            @Override
            public void onFinish() {
                countdown.cancel();
                mQuestionView.setText("");
                mEqualsView.setText("= ");
                displayQuestion(mCurrentQuestion);
                mEnableTouch = true;
            }
        };
        countdown.start();
    }

    private void displayQuestion(final Question question) {
        mQuestionView.setText(question.getQuestion());

        String tempRound = mQuestionHandler.getNextQuestionIndex() + "/" + mNumOfQuestions;
        mRoundView.setText(tempRound);
    }

    private void checkAnswer() {
        String answer = mAnswerView.getText().toString();
        String solution = Integer.toString(mCurrentQuestion.getAnswer());
        boolean isCorrect = false;

        if (answer.length() == solution.length()) {
            if (answer.equals(solution)) {
                isCorrect = true;
                mNumOfCorrect++;
                mNumOfCorrectView.setText(String.valueOf(mNumOfCorrect));
            }
            else {
                mNumOfWrong++;
                mNumOfWrongView.setText(String.valueOf(mNumOfWrong));
                vb.vibrate(100);
            }
            onAnswer(isCorrect);
        }
    }

    private void onAnswer(boolean isCorrect) {
        if (isCorrect) {
            mResultView.setText(R.string.correct_answer);
            mResultView.setTextColor(Color.GREEN);

            mAnswerView.setTextColor(Color.GREEN);
            mAnswerView.setPaintFlags(mAnswerView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        }
        else {
            mResultView.setText(R.string.wrong_answer);
            mResultView.setTextColor(Color.RED);

            mAnswerView.setTextColor(Color.RED);
            mAnswerView.setPaintFlags(mAnswerView.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            mSolutionView.setText(Integer.toString(mCurrentQuestion.getAnswer()));
            mSolutionView.setTextColor(Color.GREEN);
            mSolutionView.setPaintFlags(mSolutionView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        }
        mEnableTouch = false;
        nextQuestion();
    }

    private void nextQuestion() {
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mEnableTouch = true;
                if (--mNumOfQuestionsLeft != 0) {
                    resetAnswerView();
                    mCurrentQuestion = mQuestionHandler.getNextQuestion();
                    displayQuestion(mCurrentQuestion);
                }
                else {
                    saveStatistics();
                    exitGameDialog();
                }

            }
        }, 2000);
    }

    private void resetAnswerView() {
        mAnswerView.setText("");
        mAnswerView.setPaintFlags(0);
        mAnswerView.setTextColor(Color.parseColor("#808080"));

        mSolutionView.setText("");
        mSolutionView.setPaintFlags(0);
        mSolutionView.setTextColor(Color.parseColor("#808080"));

        mResultView.setText("");
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mEnableTouch && super.dispatchTouchEvent(ev);
    }

    private void saveStatistics() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        int numOfCorrect = preferences.getInt("num_of_correct", 0);
        int numOfWrong = preferences.getInt("num_of_wrong", 0);
        int numOfTimesPlayed = preferences.getInt("num_of_times_played", 0);

        editor.putInt("num_of_correct", mNumOfCorrect + numOfCorrect);
        editor.putInt("num_of_wrong", mNumOfWrong + numOfWrong);
        editor.putInt("num_of_times_played", ++numOfTimesPlayed);
        editor.apply();
    }

    private void exitGameDialog() {
        String title = getString(R.string.game_over_title);
        String message1 = getString(R.string.game_over_message_1);
        String message2 = getString(R.string.game_over_message_2);
        String message3 = getString(R.string.game_over_message_3);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message1 + " " + mNumOfCorrect + " " + message2 + " " + mNumOfWrong + " " + message3)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    private void createNumpad() {
        Button bt1 = findViewById(R.id.bt_1);
        bt1.setOnClickListener(this);

        Button bt2 = findViewById(R.id.bt_2);
        bt2.setOnClickListener(this);

        Button bt3 = findViewById(R.id.bt_3);
        bt3.setOnClickListener(this);

        Button bt4 = findViewById(R.id.bt_4);
        bt4.setOnClickListener(this);

        Button bt5 = findViewById(R.id.bt_5);
        bt5.setOnClickListener(this);

        Button bt6 = findViewById(R.id.bt_6);
        bt6.setOnClickListener(this);

        Button bt7 = findViewById(R.id.bt_7);
        bt7.setOnClickListener(this);

        Button bt8 = findViewById(R.id.bt_8);
        bt8.setOnClickListener(this);

        Button bt9 = findViewById(R.id.bt_9);
        bt9.setOnClickListener(this);

        Button bt0 = findViewById(R.id.bt_0);
        bt0.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_1:
                mAnswerView.append("1");
                break;
            case R.id.bt_2:
                mAnswerView.append("2");
                break;
            case R.id.bt_3:
                mAnswerView.append("3");
                break;
            case R.id.bt_4:
                mAnswerView.append("4");
                break;
            case R.id.bt_5:
                mAnswerView.append("5");
                break;
            case R.id.bt_6:
                mAnswerView.append("6");
                break;
            case R.id.bt_7:
                mAnswerView.append("7");
                break;
            case R.id.bt_8:
                mAnswerView.append("8");
                break;
            case R.id.bt_9:
                mAnswerView.append("9");
                break;
            case R.id.bt_0:
                mAnswerView.append("0");
                break;
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirm_exit)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Game.this.finish();
                    }
                })
                .setNegativeButton(R.string.confirm_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mQuestionView", mQuestionView.getText().toString());
        outState.putString("mQuestionView", mCurrentQuestion.getQuestion());
        outState.putInt("mNumOfQuestionsLeft", mNumOfQuestionsLeft);
        outState.putInt("mNumOfCorrect", mNumOfCorrect);
        outState.putInt("mNumOfWrong", mNumOfWrong);
    }

}
