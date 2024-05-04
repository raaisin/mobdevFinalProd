package com.example.mobdevfinalprod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btn1 = (Button) findViewById(R.id.btnForm);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(
                        MainActivity.this, initialForms.class
                );

                startActivity(intent1);
            }
        });

        Button btn2 = (Button) findViewById(R.id.btnMainLand);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(
                        MainActivity.this, MainLandingPage.class
                );

                startActivity(intent2);
            }
        });

        Button btn3 = (Button) findViewById(R.id.btnFitness);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(
                        MainActivity.this, FitnessPage.class
                );

                startActivity(intent3);
            }
        });

        Button btn4 = (Button) findViewById(R.id.btnWorkout);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(
                        MainActivity.this, WorkoutPage.class
                );

                startActivity(intent4);
            }
        });


    }
}