package com.example.mobdevfinalprod;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.mobdevfinalprod.helperclasses.ExerciseCreator;
import com.google.api.Distribution;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DiscoverPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DiscoverPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiscoverPage.
     */
    // TODO: Rename and change types and number of parameters
    public static DiscoverPage newInstance(String param1, String param2) {
        DiscoverPage fragment = new DiscoverPage();
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

    private List<String> exercises;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Typeface customTypeface = ResourcesCompat.getFont(getContext(), R.font.poppins);
        View view = inflater.inflate(R.layout.fragment_discover_page, container, false);
        exercises = ExerciseCreator.createExercises();
        ExerciseCreator.addExerciseToDatabase(exercises,getContext());
        LinearLayout toAddContainer = view.findViewById(R.id.discover_container);
        for(String exercise : exercises) {
            TextView exerciseText = new TextView(getContext());
            exerciseText.setText(exercise);
            exerciseText.setTypeface(customTypeface);
            exerciseText.setTextColor(Color.BLACK);
            toAddContainer.addView(exerciseText);

            exerciseText.setOnClickListener(event -> {
                Intent intent = new Intent(getContext(),InDepthExerciseView.class);
                intent.putExtra("exerciseName",exercise);
                startActivity(intent);
            });
        }
        return view;
    }
    private void createList() {

    }
}