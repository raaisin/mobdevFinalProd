package com.example.mobdevfinalprod;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.mobdevfinalprod.MainLandingPage;
import com.example.mobdevfinalprod.R;

public class initialForms extends AppCompatActivity {







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_forms);

        EditText eTextFname = (EditText) findViewById(R.id.textFirstName);
        EditText eTextLname = (EditText) findViewById(R.id.textLastName);
        EditText eTextBdate = (EditText) findViewById(R.id.textBdate);
        EditText eTextPnumber = (EditText) findViewById(R.id.textPhoneNumber);
        EditText eTextEmail =  (EditText) findViewById(R.id.textEmail);
        EditText eTextCollege = (EditText) findViewById(R.id.textCollege);
        EditText eTextStudentid = (EditText) findViewById(R.id.textStudentID);
        EditText eTextFathername = (EditText) findViewById(R.id.textFatherName);
        EditText eTextMothername = (EditText) findViewById(R.id.textMotherName);
        RadioButton rMale = (RadioButton) findViewById(R.id.radBtnMale);
        RadioButton rFemale = (RadioButton) findViewById(R.id.radBtnFemale);
        RadioButton rOthers = (RadioButton) findViewById(R.id.radBtnOthers);
        RadioButton rFirst = (RadioButton) findViewById(R.id.radBtnFirst);
        RadioButton rSecond = (RadioButton) findViewById(R.id.radBtnSecond);
        RadioButton rThird = (RadioButton) findViewById(R.id.radBtnThird);
        RadioButton rFourth = (RadioButton) findViewById(R.id.radBtnFourth);
        RadioButton rFifth = (RadioButton) findViewById(R.id.radBtnFifth);
        Button btnClear = (Button) findViewById(R.id.btnClear2);
        Button btnSub = (Button) findViewById(R.id.btnSubmitt);

        btnSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                String fname = eTextFname.getText().toString();
                String lname = eTextLname.getText().toString();
                String bdate = eTextBdate.getText().toString();
                String pnumber = eTextPnumber.getText().toString();
                String email = eTextEmail.getText().toString();
                String college = eTextCollege.getText().toString();
                String studentid = eTextStudentid.getText().toString();
                String fathername = eTextFathername.getText().toString();
                String mothername = eTextMothername.getText().toString();

                String gender;
                if(rMale.isChecked()){
                    gender = "male";
                }
                else if (rFemale.isChecked()) {
                    gender = "female";
                }
                else if (rOthers.isChecked()) {
                    gender = "others";
                }
                else{
                    gender = "unknown";
                }

                String year;
                if(rFirst.isChecked()){
                    year = "1st Year";
                }
                else if (rSecond.isChecked()) {
                    year = "2nd Year";
                }
                else if (rThird.isChecked()) {
                    year = "3rd Year";
                }
                else if (rFourth.isChecked()) {
                    year = "4th Year";
                }
                else if (rFifth.isChecked()) {
                    year = "5th Year";
                }
                else{
                    year = "unknown";
                }

                Intent intent = new Intent(initialForms.this, MainLandingPage.class);
                intent.putExtra("fname_key", fname);
                intent.putExtra("lname_key", lname);
                intent.putExtra("gender_key", gender);
                intent.putExtra("bdate_key", bdate);
                intent.putExtra("pnum_key", pnumber);
                intent.putExtra("year_key", year);
                intent.putExtra("email_key", email);
                intent.putExtra("college_key", college);
                intent.putExtra("studentid_key", studentid);
                intent.putExtra("moname_key", mothername);
                intent.putExtra("faname_key", fathername);

                startActivity(intent);




            }
        });

    }







}