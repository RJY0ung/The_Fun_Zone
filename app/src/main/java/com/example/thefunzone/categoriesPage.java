package com.example.thefunzone;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class categoriesPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories_page);

        Button numbersButton = findViewById(R.id.numbersButton);
        Button timerButton = findViewById(R.id.timerButton);


        //go to the 'shake the right answer' game
        numbersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categoriesPage.this, shakeInstructions.class);
                startActivity(intent);
            }
        });

        //go to the 'speed maths' game
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(categoriesPage.this, speedMathsInstructions.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();

    }
}
