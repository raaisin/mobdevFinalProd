package com.example.mobdevfinalprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainPage extends AppCompatActivity {
    private TabLayout tabs;
    private Fragment initialView;
    private Fragment discoverPage;
    private Fragment reportPage;
    private Fragment aiHelperPage;
    private Fragment historyPage;
    private int selectedTab = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        tabs = findViewById(R.id.main_page_tabs);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction[] transaction = {fragmentManager.beginTransaction()};
        initialView = fragmentManager.findFragmentByTag("InitialView");
        discoverPage = fragmentManager.findFragmentByTag("DiscoverPage");
        reportPage = fragmentManager.findFragmentByTag("ReportPage");
        aiHelperPage = fragmentManager.findFragmentByTag("AIHelperPage");
        historyPage = fragmentManager.findFragmentByTag("Exerciehistory");

        if (initialView == null) {
            initialView = InitialView.newInstance(username);
            transaction[0].add(R.id.changing_layout, initialView, "InitialView");
        }

        if (discoverPage == null) {
            discoverPage = DiscoverPage.newInstance(username);
            transaction[0].add(R.id.changing_layout, discoverPage, "DiscoverPage");
        }

        if (reportPage == null) {
            reportPage = new ReportPage();
            transaction[0].add(R.id.changing_layout, reportPage, "ReportPage");
        }

        if (aiHelperPage == null) {
            aiHelperPage = new AIHelperPage();
            transaction[0].add(R.id.changing_layout, aiHelperPage, "AIHelperPage");
        }
        if (historyPage == null) {
            historyPage = ExerciseHistory.newInstance(username);
            transaction[0].add(R.id.changing_layout, historyPage, "Exerciehistory");
        }

        // Hide all fragments except the initial one
        transaction[0].hide(discoverPage);
        transaction[0].hide(reportPage);
        transaction[0].hide(aiHelperPage);
        transaction[0].hide(historyPage);
        transaction[0].commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Hide all fragments
                transaction.hide(initialView);
                transaction.hide(discoverPage);
                transaction.hide(reportPage);
                transaction.hide(aiHelperPage);
                transaction.hide(historyPage);

                // Show the selected fragment
                switch (tab.getPosition()) {
                    case 0:
                        transaction.show(initialView);
                        animate(0,transaction);
                        break;
                    case 1:
                        transaction.show(discoverPage);
                        animate(1,transaction);
                        break;
                    case 2:
                        transaction.show(aiHelperPage);
                        animate(2,transaction);
                        break;
                    case 3:
                        transaction.show(reportPage);
                        animate(3,transaction);
                        break;
                    case 4:
                        transaction.show(historyPage);
                        animate(4,transaction);
                        break;
                }
//                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // No action needed for unselection
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // No action needed for reselection
            }
        });
    }
    private void animate(int index,FragmentTransaction transaction){
        if(selectedTab > index) {
            transaction.setCustomAnimations(R.anim.from_right_to_left,R.anim.center_to_right).commit();
            selectedTab = index;
        }
        else if(selectedTab < index) {
            transaction.setCustomAnimations(R.anim.from_left_to_right, R.anim.center_to_left).commit();
            selectedTab = index;
        }
    }
}