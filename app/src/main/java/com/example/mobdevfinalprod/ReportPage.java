package com.example.mobdevfinalprod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.AnimationClass;

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

        weightView = view.findViewById(R.id.weight);
        reduceWeight = view.findViewById(R.id.reduce_weight);
        addWeight = view.findViewById(R.id.add_weight);

        addGenderButtonFunction(view);
//        addWeightButtonFunction();
        reduceWeight.setOnClickListener(click->{
            int currentValue = Integer.parseInt(String.valueOf(weightView.getText()));
            weightView.setText(String.valueOf(currentValue-1));
        });
        addWeight.setOnClickListener(click->{
            int currentValue = Integer.parseInt(String.valueOf(weightView.getText()));
            weightView.setText(String.valueOf(currentValue+1));
        });

        return view;
    }
    private static void addGenderButtonFunction(View view) {
        view.findViewById(R.id.male).setOnClickListener(click-> {
            click.setBackgroundResource(R.drawable.selected_gender);
            click.startAnimation(AnimationClass.addFadeOutAnimation());
            click.startAnimation(AnimationClass.addFadeInAnimation());
            view.findViewById(R.id.female).setBackgroundResource(R.drawable.gender_button);
        });
        view.findViewById(R.id.female).setOnClickListener(click-> {
            click.setBackgroundResource(R.drawable.selected_gender);
            click.startAnimation(AnimationClass.addFadeOutAnimation());
            click.startAnimation(AnimationClass.addFadeInAnimation());
            view.findViewById(R.id.male).setBackgroundResource(R.drawable.gender_button);
        });
    }
}