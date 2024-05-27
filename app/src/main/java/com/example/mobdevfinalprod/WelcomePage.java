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
    public static RegistrationPage registrationPage;
    public static LoginPage loginPage;
    private ImageButton getting_started;
    private TextView already_have;
    private Fragment LoginPageFragment;
    private Fragment RegisterPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);

        registrationPage = new RegistrationPage();
        loginPage = new LoginPage();
        framelayout = findViewById(R.id.frame_layout);

        getting_started = findViewById(R.id.welcome_register_button);
        getting_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, registrationPage).commit();
            }
        });
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        LoginPageFragment = new LoginPage();
        RegisterPageFragment = new RegistrationPage();
        already_have = findViewById(R.id.go_to_login);
        already_have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().add(R.id.frame_layout, loginPage).commit();
            }
        });
    }
    public static void changeToRegistration(FragmentManager fragmentManager, RegistrationPage registrationPage) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.from_left_to_right, 0);
        transaction.replace(R.id.frame_layout, registrationPage);
        transaction.commit();
    }
    public static void changeToLogin(FragmentManager fragmentManager, LoginPage loginPage){
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.from_right_to_left, 0);
        transaction.replace(R.id.frame_layout,loginPage);
        transaction.commit();
    }
}