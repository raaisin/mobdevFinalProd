package com.example.mobdevfinalprod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportPage.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportPage newInstance(String param1, String param2) {
        ReportPage fragment = new ReportPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_page, container, false);
        addGenderButtonFunction(view);
        return view;
    }
    private void addGenderButtonFunction(View view) {
        view.findViewById(R.id.male).setOnClickListener(click-> {
            Toast.makeText(getContext(), "Male Clicked", Toast.LENGTH_LONG).show();
            // change to something
//            click.setBackgroundResource(R.drawable.bottom_nav_background);
//            view.findViewById(R.id.female).setBackgroundResource(R.drawable.gender_button);
        });
        view.findViewById(R.id.female).setOnClickListener(click-> {
            Toast.makeText(getContext(), "Female Clicked", Toast.LENGTH_LONG).show();
            // change to something
//            click.setBackgroundResource(R.drawable.bottom_nav_background);
//            view.findViewById(R.id.male).setBackgroundResource(R.drawable.gender_button);
        });
    }
}