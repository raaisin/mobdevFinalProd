package com.example.mobdevfinalprod;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainLandingPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //User Profile

        TextView fname = findViewById(R.id.tvFname);
        TextView lname = findViewById(R.id.tvLName);
        TextView gender = findViewById(R.id.tvGender1);
        TextView birthdate = findViewById(R.id.tvBDate);
        TextView phonenumber = findViewById(R.id.tvPhone);
        TextView email = findViewById(R.id.tvEmail);
        TextView yrlevel = findViewById(R.id.tvYear);
        TextView college = findViewById(R.id.tvCollege);
        TextView studentid = findViewById(R.id.tvID);
        TextView fathername = findViewById(R.id.tvFather);
        TextView mothername = findViewById(R.id.tvMother);

        Intent intent = getIntent();
        String fname2 = intent.getStringExtra("fname_key");
        String lname2 = intent.getStringExtra("lname_key");
        String gender2 = intent.getStringExtra("gender_key");
        String birthdate2 = intent.getStringExtra("bdate_key");
        String phonenumber2 = intent.getStringExtra("pnum_key");
        String email2 = intent.getStringExtra("email_key");
        String yrlevel2 = intent.getStringExtra("year_key");
        String college2 = intent.getStringExtra("college_key");
        String studentid2 = intent.getStringExtra("studentid_key");
        String fathername2 = intent.getStringExtra("moname_key");
        String mothername2 = intent.getStringExtra("faname_key");

        fname.setText(fname2);
        lname.setText(lname2);
        gender.setText(gender2);
        birthdate.setText(birthdate2);
        phonenumber.setText(phonenumber2);
        email.setText(email2);
        yrlevel.setText(yrlevel2);
        college.setText(college2);
        studentid.setText(studentid2);
        fathername.setText(fathername2);
        mothername.setText(mothername2);

        //User Profile end

        Button btn1 = (Button) findViewById(R.id.btnFit);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(
                        MainLandingPage.this, FitnessPage.class
                );

                startActivity(intent1);
            }
        });

        Button btn2 = (Button) findViewById(R.id.btnWork);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(
                        MainLandingPage.this, WorkoutPage.class
                );

                startActivity(intent2);
            }
        });
    }
}