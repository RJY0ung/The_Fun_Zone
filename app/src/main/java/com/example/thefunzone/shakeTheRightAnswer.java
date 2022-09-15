package com.example.thefunzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class shakeTheRightAnswer extends AppCompatActivity {

    //equation statement
    private TextView equationStatement;

    //array list for random number
    private ArrayList<Integer> numberArrayList = new ArrayList<>();
    private TextView showArrayList;
    private int integerNum;


    //timer
    private TextView timer;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis;
    private static final long COUNTDOWN_IN_MILLIS = 60000;
    private String showNum;

    //score
    private TextView shakeScore;
    private static final String KEY_SCORE = "keyScore";
    private int score;
    private boolean checkIfRightAnswer;

    //shared preferences from shakeInstructions page
    String chosenLevel;
    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    //for shared preferences for the final score
    public static final String SHAKE_ANSWER_SCORE = "shakeAnswerScore";


    //for sensor programming
    private SensorManager sensorManager;

    private float acelVal; //current acceleration value and gravity
    private float acelLast; //last acceleration value and gravity
    private float shake; //acceleration value differ from gravity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shake_the_right_answer);

        //initialize

        //equation statement
        equationStatement = findViewById(R.id.equationStatement);

        //for countdown timer
        showArrayList = findViewById(R.id.showArrayList);
        timer = findViewById(R.id.timer);

        //for the score
        shakeScore = findViewById(R.id.shakeScore);

        //get the chosen level from shared preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Intent intent = getIntent();
        chosenLevel = intent.getStringExtra(shakeInstructions.DIFFICULTY_LEVEL);

        //set equation statement
        if (chosenLevel == null){
            equationStatement.setText("Multiples of 2");
        } else if(chosenLevel.endsWith("Easy")){
            equationStatement.setText("Multiples of 2");
        } else if(chosenLevel.endsWith("Medium")){
            equationStatement.setText("Multiples of 3");
        } else{
            equationStatement.setText("Multiples of 7");
        }

        //adding the numbers to the array list
        numberArrayList.add(1);
        numberArrayList.add(2);
        numberArrayList.add(3);
        numberArrayList.add(4);
        numberArrayList.add(5);
        numberArrayList.add(6);
        numberArrayList.add(7);
        numberArrayList.add(8);
        numberArrayList.add(9);
        numberArrayList.add(10);
        numberArrayList.add(11);
        numberArrayList.add(12);
        numberArrayList.add(13);
        numberArrayList.add(14);
        numberArrayList.add(15);

        //for sensor programming
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        acelVal = SensorManager.GRAVITY_EARTH;
        acelLast = SensorManager.GRAVITY_EARTH;
        shake = 0.0f;

        //for timer
        timeLeftInMillis = COUNTDOWN_IN_MILLIS;
        startCountDown();
        numberChange();

    }

    //random number from array list
    public int getRandomNumberFromList(ArrayList<Integer> numberArrayList){
        Random rand = new Random();
        return numberArrayList.get(rand.nextInt(numberArrayList.size()));
    }

    //countdown timer
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
                finishGame();
            }
        }.start();
    }

    //for the number
    private void numberChange(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateNumberChange();
            }

            @Override
            public void onFinish() {
                timeLeftInMillis = 0;
                finishGame();
            }
        }.start();
    }

    private void updateCountDownText(){
        int minutes =(int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timer.setText(timeFormatted);

        if(timeLeftInMillis > 10000){
            timer.setTextColor(Color.BLACK);
        } else {
            timer.setTextColor(Color.RED);
        }
    }

    //for the number
    private void updateNumberChange(){
        if(timeLeftInMillis > 0){
            integerNum = getRandomNumberFromList(numberArrayList);
            int index = numberArrayList.indexOf(integerNum);

            //remove number from ArrayList once it has been displayed
            numberArrayList.remove(index);

            //once all numbers have been removed from the ArrayList
            boolean emptyList = numberArrayList.isEmpty();
            if (emptyList == true){
                finishGame();
            }



            showNum = Integer.toString(integerNum);
            showArrayList.setText(showNum);
        }
    }

    private void finishGame(){
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHAKE_ANSWER_SCORE, score);
        setResult(RESULT_OK, resultIntent);
        Toast.makeText(getBaseContext(), "Well Done! You have scored: " + score + " points", Toast.LENGTH_LONG).show();
        finish();
    }

    // all code below are for sensor programming
    private final SensorEventListener sensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {

            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            acelLast = acelVal;
            acelVal = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = acelVal - acelLast;
            shake = shake * 0.9f + delta;

            //what happens when user shakes the phone
            if(shake > 3){
                //Using toast as a test if shake is working
                //Toast.makeText(getApplicationContext(), "SHAKING WORKS", Toast.LENGTH_LONG).show();

                //check if answer is correct
                if(chosenLevel == null){
                    //Using toast as a test
                    checkAnswerOne();
                } else if(chosenLevel.endsWith("Easy")){
                    checkAnswerOne();

                } else if(chosenLevel.endsWith("Medium")){
                    checkAnswerTwo();
                } else {
                    checkAnswerThree();
                }
                //checkAnswerOne();
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    //level 1
    private void checkAnswerOne(){
        checkIfRightAnswer = true;

        if(integerNum % 2 == 0){
            score++;
            shakeScore.setText("Score: " + score);
        }
    }

    //level 2
    private void checkAnswerTwo(){
        checkIfRightAnswer = true;

        if(integerNum % 3 == 0){
            score++;
            shakeScore.setText("Score: " + score);
        }
    }


    //level 3
    private void checkAnswerThree(){
        checkIfRightAnswer = true;

        if(integerNum % 7 == 0){
            score++;
            shakeScore.setText("Score: " + score);
        }
    }



    //on start method called
    @Override
    protected void onStart(){
        super.onStart();
    }

    //on destroy method called
    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(countDownTimer != null){
            countDownTimer.cancel();
        }
    }

}
