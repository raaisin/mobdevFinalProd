package com.example.mobdevfinalprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransition;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

public class WelcomePage extends AppCompatActivity {
    private LinearLayout framelayout;
    private RegistrationPage registrationPage;
    private LoginPage loginPage;
    private ImageButton getting_started;
    private TextView already_have;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        framelayout = findViewById(R.id.frame_layout);

        getting_started = findViewById(R.id.welcome_register_button);
        already_have = findViewById(R.id.go_to_login);

        // Initialize fragments
        if (registrationPage == null) {
            registrationPage = new RegistrationPage();
        }
        if (loginPage == null) {
            loginPage = new LoginPage();
        }


        getting_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToRegistration(getSupportFragmentManager());
            }
        });

        already_have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToLogin(getSupportFragmentManager());
            }
        });
    }

    public static void changeToRegistration(FragmentManager fragmentManager) {
        Fragment registrationPage = fragmentManager.findFragmentByTag("RegistrationPage");
        if (registrationPage == null) {
            registrationPage = new RegistrationPage();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.from_left_to_right, R.anim.center_to_right);
        transaction.replace(R.id.frame_layout, registrationPage, "RegistrationPage");
        transaction.commit();
    }

    public static void changeToLogin(FragmentManager fragmentManager) {
        Fragment loginPage = fragmentManager.findFragmentByTag("LoginPage");
        if (loginPage == null) {
            loginPage = new LoginPage();
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.from_right_to_left, R.anim.center_to_left);
        transaction.replace(R.id.frame_layout, loginPage, "LoginPage");
        transaction.commit();
    }
}