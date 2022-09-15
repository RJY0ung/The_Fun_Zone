package com.example.thefunzone;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class settingsPage extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner difficultyLevelSpinner;
    private Button okButton;

    //to be shared in the game pages
    public static final String SELECTED_LEVEL = "selectedLevel";

    //test
    private static final String SAVE_LEVEL = "saveLevel";
    private SharedPreferences sharedPreferences;
    private static final String ARRAY_NUMBER = "arrayNumber";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);


        //initialize
        difficultyLevelSpinner = findViewById(R.id.difficultyLevelSpinner);
        okButton = findViewById(R.id.okButton);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Levels, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultyLevelSpinner.setAdapter(adapter);
        difficultyLevelSpinner.setOnItemSelectedListener(this);

        //shared preferences
        sharedPreferences = getSharedPreferences(SAVE_LEVEL, MODE_PRIVATE);





    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        final String selectedLevel = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), selectedLevel + " selected", Toast.LENGTH_SHORT).show();


        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settingsPage.this, shakeInstructions.class);
                intent.putExtra(SELECTED_LEVEL, selectedLevel);
                startActivity(intent);

            }
        });

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {


    }


    @Override
    protected void onStop() {
        super.onStop();

        int arrayNumber = difficultyLevelSpinner.getSelectedItemPosition();
        sharedPreferences.edit().putInt(ARRAY_NUMBER, arrayNumber).apply();


    }

    @Override
    protected void onStart() {
        super.onStart();

        int updatedArrayNumber = sharedPreferences.getInt(ARRAY_NUMBER, 0);
        difficultyLevelSpinner.setSelection(updatedArrayNumber);

    }
}
