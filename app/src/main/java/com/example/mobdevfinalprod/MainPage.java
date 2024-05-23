package com.example.mobdevfinalprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainPage extends AppCompatActivity {
    private TabLayout tabs;private Fragment initialViewFragment;
    private Fragment discoverPageFragment;
    private Fragment reportPageFragment;
    private Fragment aIHelperFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        tabs = findViewById(R.id.main_page_tabs);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        initialViewFragment = new InitialView(username);
        discoverPageFragment = new DiscoverPage(username);
        reportPageFragment = new ReportPage();
        aIHelperFragment = new AIHelperPage();

        // Add fragments to the FragmentManager
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.changing_layout, initialViewFragment, "InitialView");
        transaction.add(R.id.changing_layout, discoverPageFragment, "DiscoverPage").hide(discoverPageFragment);
        transaction.add(R.id.changing_layout, reportPageFragment, "ReportPage").hide(reportPageFragment);
        transaction.add(R.id.changing_layout, aIHelperFragment, "AIHelperPage").hide(aIHelperFragment);
        transaction.commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (tab.getPosition()) {
                    case 0:
                        transaction.hide(discoverPageFragment);
                        transaction.hide(reportPageFragment);
                        transaction.hide(aIHelperFragment);
                        transaction.show(initialViewFragment);
                        break;
                    case 1:
                        transaction.hide(initialViewFragment);
                        transaction.hide(reportPageFragment);
                        transaction.hide(aIHelperFragment);
                        transaction.show(discoverPageFragment);
                        break;
                    case 2:
                        transaction.hide(initialViewFragment);
                        transaction.hide(discoverPageFragment);
                        transaction.hide(aIHelperFragment);
                        transaction.show(reportPageFragment);
                        break;
                    case 3:
                        transaction.hide(initialViewFragment);
                        transaction.hide(discoverPageFragment);
                        transaction.hide(reportPageFragment);
                        transaction.show(aIHelperFragment);
                        break;
                }

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}