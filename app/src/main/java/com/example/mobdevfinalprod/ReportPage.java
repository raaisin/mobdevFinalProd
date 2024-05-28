package com.example.mobdevfinalprod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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


    public double calculateCalorie(double height, double weight, int age, boolean gender){

        double BMR;
        if (gender) {
            BMR = (10.0 * weight) + (6.25 * height) - (5.0 * age) - 161.0;
        } else {
            BMR = (10.0 * weight) + (6.25 * height) - (5.0 * age) + 5.0;
        }

        return BMR;

    }

    public double calculateBMI(double height, double weight){

        double BMI;

        BMI = (weight)/((height*100) * (height*100));

        return BMI;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_page, container, false);


            EditText heightBMI = view.findViewById(R.id.editHeightBMI);
            EditText weightBMI = view.findViewById(R.id.editWeightBMI);
            EditText resultBMI = view.findViewById(R.id.editResultBMI);

            EditText heightCalorie = view.findViewById(R.id.editHeightCalorie);
            EditText weightCalorie = view.findViewById(R.id.editWeightCalorie);

            EditText ageCalorie = view.findViewById(R.id.editAgeCalorie);

            RadioButton rMale = view.findViewById(R.id.radioMale);
            RadioButton rFemale = view.findViewById(R.id.radioFemale);

            RadioButton rFirst = view.findViewById(R.id.radioFit1);
            RadioButton rSecond = view.findViewById(R.id.radioFit2);
            RadioButton rThird = view.findViewById(R.id.radioFit3);
            RadioButton rFourth = view.findViewById(R.id.radioFit4);
            RadioButton rFifth = view.findViewById(R.id.radioFit5);

            TextView tvModerate = view.findViewById(R.id.TVModerate);
            TextView tvMild = view.findViewById(R.id.TVMild);
            TextView tvNormal = view.findViewById(R.id.TVNormal);
            TextView tvExtreme = view.findViewById(R.id.TVExtreme);


            if (heightBMI != null && !heightBMI.getText().toString().isEmpty()) {
                double BMIHeight = Double.parseDouble(heightBMI.getText().toString());
                double BMIWeight = Double.parseDouble(weightBMI.getText().toString());

                resultBMI.setText(String.valueOf(calculateBMI(BMIHeight, BMIWeight)));
            }





            int age = Integer.parseInt(ageCalorie.getText().toString());

            boolean gender;
            if(rMale.isChecked()){
                gender = false;
            }
            if(rFemale.isChecked()){
                gender = true;
            }


            double multiplier;
            if(rFirst.isChecked()){
                multiplier = 0.20;
            }
            else if (rSecond.isChecked()) {
                multiplier = 0.38;
            }
            else if (rThird.isChecked()) {
                multiplier = 0.47;
            }
            else if (rFourth.isChecked()) {
                multiplier = 0.55;
            }
            else if (rFifth.isChecked()) {
                multiplier = 0.73;
            }

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