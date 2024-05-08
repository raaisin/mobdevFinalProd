package com.example.mobdevfinalprod;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

public class MainPage extends AppCompatActivity {
    private TabLayout tabs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        tabs = findViewById(R.id.main_page_tabs);


        getSupportFragmentManager().beginTransaction().replace(R.id.changing_layout, new InitialView())
                .addToBackStack(null).commit();
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Fragment fragment = null;

                switch (tab.getPosition()) {
                    case 0:
                        fragment = new InitialView();
                        break;
                    case 1:
                        fragment = new DiscoverPage();
                        break;
                    case 2:
                        fragment = new ReportPage();
                        break;
                    case 3:
                        fragment = new AIHelperPage();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.changing_layout, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
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