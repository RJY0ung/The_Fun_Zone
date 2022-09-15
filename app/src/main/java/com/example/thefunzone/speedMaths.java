package com.example.thefunzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class speedMaths extends AppCompatActivity {

    //for shared preferences for the final score
    public static final String SPEED_MATHS_SCORE = "speedMathsScore";

    private static final String KEY_SCORE = "keyScore";
    private static final String KEY_QUESTION_COUNT = "keyQuestionCount";
    private static final String KEY_MILLIS_LEFT = "keyMillisLeft";
    private static final String KEY_ANSWERED = "keyAnswered";
    private static final String KEY_QUESTION_LIST = "keyQuestionList";

    //countdown timer
    private static final long COUNTDOWN_IN_MILLIS = 30000;


    private TextView questionsText;
    private TextView scoreText;
    private TextView numberOfQuestionsText;
    private TextView timer;
    private TextView difficultyText;
    private RadioGroup radioGroup;
    private RadioButton rb1;
    private RadioButton rb2;
    private RadioButton rb3;
    private RadioButton rb4;
    private Button confirmButton;

    //colors
    private ColorStateList textColorDefaultRb;
    private ColorStateList textColorDefaultCd;

    //timer
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;

    //questions array
    private ArrayList<Questions> questionsList;

    private int questionCounter;
    private int questionCountTotal;
    private Questions currentQuestion;

    //score
    private int score;

    //if a question has been answered
    private boolean answered;

    //if player presses the back button
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speed_maths);

        questionsText = findViewById(R.id.questionsText);
        scoreText = findViewById(R.id.scoreText);
        numberOfQuestionsText = findViewById(R.id.numberOfQuestionsText);
        difficultyText = findViewById(R.id.difficultyText);
        timer = findViewById(R.id.timer);
        radioGroup = findViewById(R.id.radio_group);
        rb1 = findViewById(R.id.radio_button1);
        rb2 = findViewById(R.id.radio_button2);
        rb3 = findViewById(R.id.radio_button3);
        rb4 = findViewById(R.id.radio_button4);
        confirmButton = findViewById(R.id.confirmButton);

        textColorDefaultRb = rb1.getTextColors();
        textColorDefaultCd = timer.getTextColors();

        Intent intent = getIntent();
        String difficulty = intent.getStringExtra(speedMathsInstructions.EXTRA_DIFFICULTY);

        difficultyText.setText("Difficulty: " + difficulty);

        if(savedInstanceState == null){

            QuizDBHelper dbHelper = new QuizDBHelper(this);
            questionsList = dbHelper.getQuestions(difficulty);
            questionCountTotal = questionsList.size();
            Collections.shuffle(questionsList);

            showNextQuestion();

        } else{
            questionsList = savedInstanceState.getParcelableArrayList(KEY_QUESTION_LIST);
            questionCountTotal = questionsList.size();
            questionCounter = savedInstanceState.getInt(KEY_QUESTION_COUNT);
            currentQuestion = questionsList.get(questionCounter - 1);
            score = savedInstanceState.getInt(KEY_SCORE);
            timeLeftInMillis = savedInstanceState.getLong(KEY_MILLIS_LEFT);
            answered = savedInstanceState.getBoolean(KEY_ANSWERED);

            if(!answered){
                startCountDown();
            } else{
                updateCountDownText();
                showSolution();
            }
        }



        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!answered){
                    if(rb1.isChecked() || rb2.isChecked() || rb3.isChecked() || rb4.isChecked()){
                        checkAnswer();
                    } else{
                        Toast.makeText(speedMaths.this, "Please select an answer", Toast.LENGTH_SHORT).show();
                    }
                } else{
                    showNextQuestion();
                }
            }
        });
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();
    }

    private void showNextQuestion(){
        rb1.setTextColor(textColorDefaultRb);
        rb2.setTextColor(textColorDefaultRb);
        rb3.setTextColor(textColorDefaultRb);
        rb4.setTextColor(textColorDefaultRb);
        radioGroup.clearCheck();

        if(questionCounter < questionCountTotal){
            currentQuestion = questionsList.get(questionCounter);

            questionsText.setText(currentQuestion.getQuestion());
            rb1.setText(currentQuestion.getOption1());
            rb2.setText(currentQuestion.getOption2());
            rb3.setText(currentQuestion.getOption3());
            rb4.setText(currentQuestion.getOption4());

            questionCounter++;
            numberOfQuestionsText.setText("Question: " + questionCounter + "/" + questionCountTotal);
            answered = false;
            confirmButton.setText("Confirm");

        } else{
            finishQuiz();
        }
    }

    private void startCountDown(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                Toast.makeText(getBaseContext(), "Time is up! You have scored: " + score + " points.", Toast.LENGTH_LONG).show();
                finishQuiz();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes =(int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);

        if(timeLeftInMillis > 10000){
            timer.setTextColor(textColorDefaultCd);
        }else{
            timer.setTextColor(Color.RED);
        }
    }

    private void checkAnswer(){
        answered = true;

        RadioButton rbSelected = findViewById(radioGroup.getCheckedRadioButtonId());
        int answerNumber = radioGroup.indexOfChild(rbSelected) + 1;

        if(answerNumber == currentQuestion.getAnswerNum()){
            score++;
            scoreText.setText("Score: " + score);
        }

        showSolution();

    }

    private void showSolution(){
        rb1.setTextColor(Color.RED);
        rb2.setTextColor(Color.RED);
        rb3.setTextColor(Color.RED);
        rb4.setTextColor(Color.RED);

        switch (currentQuestion.getAnswerNum()){
            case 1:
                rb1.setTextColor(Color.GREEN);
                questionsText.setText("Answer 1 is correct");
                break;
            case 2:
                rb2.setTextColor(Color.GREEN);
                questionsText.setText("Answer 2 is correct");
                break;
            case 3:
                rb3.setTextColor(Color.GREEN);
                questionsText.setText("Answer 3 is correct");
                break;
            case 4:
                rb4.setTextColor(Color.GREEN);
                questionsText.setText("Answer 4 is correct");
                break;
        }

        if(questionCounter < questionCountTotal){
            confirmButton.setText("Next");
        } else{
            confirmButton.setText("Finish");
        }
    }

    private void finishQuiz(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SPEED_MATHS_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if(backPressedTime + 3000 > System.currentTimeMillis()){
            finishQuiz();
        }else{
            Toast.makeText(this, "Press back again to finish the quiz and return to the instructions page",
                    Toast.LENGTH_SHORT).show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);

        outState.putInt(KEY_SCORE, score);
        outState.putInt(KEY_QUESTION_COUNT, questionCounter);
        outState.putLong(KEY_MILLIS_LEFT, timeLeftInMillis);
        outState.putBoolean(KEY_ANSWERED, answered);
        outState.putParcelableArrayList(KEY_QUESTION_LIST, questionsList);

    }


}
