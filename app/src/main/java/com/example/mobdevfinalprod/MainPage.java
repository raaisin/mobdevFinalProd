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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        tabs = findViewById(R.id.main_page_tabs);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        initialView = fragmentManager.findFragmentByTag("InitialView");
        discoverPage = fragmentManager.findFragmentByTag("DiscoverPage");
        reportPage = fragmentManager.findFragmentByTag("ReportPage");
        aiHelperPage = fragmentManager.findFragmentByTag("AIHelperPage");

        if (initialView == null) {
            initialView = InitialView.newInstance(username);
            transaction.add(R.id.changing_layout, initialView, "InitialView");
        }

        if (discoverPage == null) {
            discoverPage = DiscoverPage.newInstance(username);
            transaction.add(R.id.changing_layout, discoverPage, "DiscoverPage");
        }

        if (reportPage == null) {
            reportPage = new ReportPage();
            transaction.add(R.id.changing_layout, reportPage, "ReportPage");
        }

        if (aiHelperPage == null) {
            aiHelperPage = new AIHelperPage();
            transaction.add(R.id.changing_layout, aiHelperPage, "AIHelperPage");
        }

        // Hide all fragments except the initial one
        transaction.hide(discoverPage);
        transaction.hide(reportPage);
        transaction.hide(aiHelperPage);
        transaction.commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Hide all fragments
                transaction.hide(initialView);
                transaction.hide(discoverPage);
                transaction.hide(reportPage);
                transaction.hide(aiHelperPage);

                // Show the selected fragment
                switch (tab.getPosition()) {
                    case 0:
                        transaction.show(initialView);
                        break;
                    case 1:
                        transaction.show(discoverPage);
                        break;
                    case 2:
                        transaction.show(reportPage);
                        break;
                    case 3:
                        transaction.show(aiHelperPage);
                        break;
                }

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                transaction.commit();
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
}