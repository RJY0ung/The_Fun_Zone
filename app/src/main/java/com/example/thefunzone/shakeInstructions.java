package com.example.thefunzone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class shakeInstructions extends AppCompatActivity {

    public Button startButton;
    public Button backButton;

    //to get the result
    private static final int REQUEST_CODE_SHAKE = 1;
    private int highScore;
    private TextView highScoresText;
    public static final String KEY_SHAKE_HIGH_SCORE = "keyShakeHighScore";

    //shared preferences from settings page
    String chosenLevel;
    private SharedPreferences sharedPreferences;
    public static final String SHARED_PREFS = "sharedPrefs";

    //shared preferences to send to game page
    public static final String DIFFICULTY_LEVEL = "difficultyLevel";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shake_instructions);

        //start button
        startButton = findViewById(R.id.startButton);

        //back button
        backButton = findViewById(R.id.backButton);

        //for the high score text
        highScoresText = findViewById(R.id.highScoresText);

        //get the chosen level from shared preferences
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Intent intent = getIntent();
        chosenLevel = intent.getStringExtra(settingsPage.SELECTED_LEVEL);

        loadHighScore();

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //difficulty level chosen
                Intent intent = new Intent(shakeInstructions.this, shakeTheRightAnswer.class);
                intent.putExtra(DIFFICULTY_LEVEL, chosenLevel);
                startActivityForResult(intent, REQUEST_CODE_SHAKE);

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(shakeInstructions.this, categoriesPage.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_SHAKE){
            if(resultCode == RESULT_OK){
                int score = data.getIntExtra(shakeTheRightAnswer.SHAKE_ANSWER_SCORE, 0);

                if(score > highScore){
                    updateHighScore(score);
                }

            }
        }
    }

    private void loadHighScore(){
        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        highScore = prefs.getInt(KEY_SHAKE_HIGH_SCORE, 0);
        highScoresText.setText("High Score: " + highScore);
    }

    private void updateHighScore(int highScoreNew){
        highScore = highScoreNew;
        highScoresText.setText("High Score: " + highScore);

        SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(KEY_SHAKE_HIGH_SCORE, highScore);
        editor.apply();
    }

    //settings in options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);

        return true;
    }

    //go to settings page
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = new Intent(shakeInstructions.this, settingsPage.class);
        startActivity(intent);

        return super.onOptionsItemSelected(item);
    }
}
