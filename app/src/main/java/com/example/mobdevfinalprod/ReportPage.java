package com.example.mobdevfinalprod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.AnimationClass;

import java.util.EventListener;

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
    private static TextView weightView;
    private static ImageButton reduceWeight;
    private static ImageButton addWeight;
    private static ImageButton addAge;
    private static ImageButton reduceAge;
    private static TextView ageView;
    private static ImageButton see_results;
    private static EditText heightView;
    public static String gender;
    static LinearLayout ageContainer;
    static LinearLayout heightContainer;
    static LinearLayout weightContainer;

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
        view.findViewById(R.id.result_container).setVisibility(View.GONE);
        view.findViewById(R.id.loading_bmi).setVisibility(View.GONE);
        view.findViewById(R.id.overall_result).setVisibility(View.GONE);
        gender = null;
        addButtonFunctions(view);
        addGenderButtonFunction(view);
        return view;
    }
    private void addButtonFunctions(View view) {
        heightView = view.findViewById(R.id.height);
        weightView = view.findViewById(R.id.weight);
        reduceWeight = view.findViewById(R.id.reduce_weight);
        addWeight = view.findViewById(R.id.add_weight);
        ageView = view.findViewById(R.id.age);
        reduceAge = view.findViewById(R.id.reduce_age);
        addAge = view.findViewById(R.id.add_age);
        see_results = view.findViewById(R.id.submit_BMI);
        heightContainer = view.findViewById(R.id.height_container);
        weightContainer = view.findViewById(R.id.weight_container);
        ageContainer = view.findViewById(R.id.age_container);
        heightView.setOnClickListener(click->{
            setHeightClick();
        });

        reduceWeight.setOnClickListener(click->{
            int currentValue = Integer.parseInt(String.valueOf(weightView.getText()));
            weightView.setText(String.valueOf(currentValue-1));
            setWeightClicks();
        });
        addWeight.setOnClickListener(click->{
            int currentValue = Integer.parseInt(String.valueOf(weightView.getText()));
            weightView.setText(String.valueOf(currentValue+1));
            setWeightClicks();
        });
        reduceAge.setOnClickListener(click->{
            int currentValue = Integer.parseInt(String.valueOf(ageView.getText()));
            ageView.setText(String.valueOf(currentValue-1));
            setAgeClick();
        });
        addAge.setOnClickListener(click->{
            int currentValue = Integer.parseInt(String.valueOf(ageView.getText()));
            ageView.setText(String.valueOf(currentValue+1));
            setAgeClick();
        });
        see_results.setOnClickListener(click->{
            ageContainer.setBackgroundResource(R.drawable.gender_button);
            heightContainer.setBackgroundResource(R.drawable.gender_button);
            weightContainer.setBackgroundResource(R.drawable.gender_button);

            if(gender == null) {
                Toast.makeText(getContext(), "Please select a gender", Toast.LENGTH_SHORT).show();
                return;
            }
            if(((TextView)view.findViewById(R.id.height)).getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please enter a height", Toast.LENGTH_SHORT).show();
                return;
            }
            view.findViewById(R.id.overall_result).setVisibility(View.GONE);
            view.findViewById(R.id.result_container).setVisibility(View.VISIBLE);
            view.findViewById(R.id.loading_bmi).setVisibility(View.VISIBLE);
            double weight = Double.parseDouble(String.valueOf(weightView.getText()));
            double height = (Double.parseDouble(String.valueOf(heightView.getText())))/100;
            double bmi = weight / (height * height);
            scrollDown(view.findViewById(R.id.scrollView));
            int age = Integer.parseInt(String.valueOf(ageView.getText()));
            ((TextView)view.findViewById(R.id.bmi_value)).setText(String.format("%.2f",bmi));
            String prompt = "With the BMI of:" + String.format("%.2f",bmi) + " Gender: " + gender + " Age: " + age +
                    ". How much calories should an individual consume to lose, maintain, and gain weight ONLY.";
            AIHelperPage.getAIResponse(prompt, new AIHelperPage.ResponseCallback() {
                @Override
                public void onResponse(String response) {
                    response = response.replace("*","");

                    String finalResponse = response;
                    getActivity().runOnUiThread(()->{
                        view.findViewById(R.id.overall_result).startAnimation(AnimationClass.addFadeInAnimation());
                        view.findViewById(R.id.overall_result).setVisibility(View.VISIBLE);
                        ((TextView)view.findViewById(R.id.overall_result)).setText(finalResponse);
                        view.findViewById(R.id.loading_bmi).setVisibility(View.GONE);
                        scrollDown(view.findViewById(R.id.scrollView));
                    });
                }
            });
            AIHelperPage.getAIResponse("Only one word or two, what is the category for a person with a bmi of" + bmi, new AIHelperPage.ResponseCallback() {
                @Override
                public void onResponse(String response) {
                    getActivity().runOnUiThread(()->{
                        ((TextView)view.findViewById(R.id.bmi_category)).setText(response);
                    });
                }
            });
        });
    }
    private static void setGenderClick(){
        weightContainer.setBackgroundResource(R.drawable.gender_button);
        heightContainer.setBackgroundResource(R.drawable.gender_button);
        ageContainer.setBackgroundResource(R.drawable.gender_button);
    }
    private static void setWeightClicks() {
        weightContainer.setBackgroundResource(R.drawable.selected_gender);
        heightContainer.setBackgroundResource(R.drawable.gender_button);
        ageContainer.setBackgroundResource(R.drawable.gender_button);
    }
    private static void setHeightClick() {
        heightContainer.setBackgroundResource(R.drawable.selected_gender);
        weightContainer.setBackgroundResource(R.drawable.gender_button);
        ageContainer.setBackgroundResource(R.drawable.gender_button);
    }
    private static void setAgeClick() {
        ageContainer.setBackgroundResource(R.drawable.selected_gender);
        heightContainer.setBackgroundResource(R.drawable.gender_button);
        weightContainer.setBackgroundResource(R.drawable.gender_button);
    }

    public static void scrollDown(ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
    private static void addGenderButtonFunction(View view) {
        view.findViewById(R.id.male).setOnClickListener(click-> {
            click.setBackgroundResource(R.drawable.selected_gender);
            click.startAnimation(AnimationClass.addFadeOutAnimation());
            click.startAnimation(AnimationClass.addFadeInAnimation());
            view.findViewById(R.id.female).setBackgroundResource(R.drawable.gender_button);
            gender = "male";
            setGenderClick();
        });
        view.findViewById(R.id.female).setOnClickListener(click-> {
            click.setBackgroundResource(R.drawable.selected_gender);
            click.startAnimation(AnimationClass.addFadeOutAnimation());
            click.startAnimation(AnimationClass.addFadeInAnimation());
            view.findViewById(R.id.male).setBackgroundResource(R.drawable.gender_button);
            gender = "female";
            setGenderClick();
        });
    }
}