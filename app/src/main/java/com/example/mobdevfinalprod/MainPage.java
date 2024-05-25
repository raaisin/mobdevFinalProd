package com.example.mobdevfinalprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainPage extends AppCompatActivity {
    private TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        tabs = findViewById(R.id.main_page_tabs);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        // Add fragments to the FragmentManager
        FragmentTransaction initialTransaction = getSupportFragmentManager().beginTransaction();
        initialTransaction.add(R.id.changing_layout, InitialView.newInstance(username), "InitialView");
        initialTransaction.commit();

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                Fragment selectedFragment = null;

                switch (tab.getPosition()) {
                    case 0:
                        selectedFragment = InitialView.newInstance(username);
                        break;
                    case 1:
                        selectedFragment = DiscoverPage.newInstance(username);
                        break;
                    case 2:
                        selectedFragment = new ReportPage();
                        break;
                    case 3:
                        selectedFragment = new AIHelperPage();
                        break;
                }

                if (selectedFragment != null) {
                    transaction.replace(R.id.changing_layout, selectedFragment);
                    transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    transaction.commit();
                }
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