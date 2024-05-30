package com.example.mobdevfinalprod;

import static android.content.ContentValues.TAG;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.AnimationClass;
import com.example.mobdevfinalprod.helperclasses.DateUtils;
import com.example.mobdevfinalprod.helperclasses.ViewUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitialView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitialView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_USERNAME = "username";
    TextView username_container;
    private String username;
    static LinearLayout welcome_container;
    static LinearLayout first_linear_layout;
    static LinearLayout second_linear_layout;
    static LinearLayout third_linear_layout;

    public InitialView() {
        // Required empty public constructor
    }
    public static InitialView newInstance(String username) {
        InitialView fragment = new InitialView();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_view, container, false);
        DateUtils.displayDates(view);
        username_container = view.findViewById(R.id.username_init);
        username_container.setText(username);
        welcome_container = view.findViewById(R.id.welcome_container);
        first_linear_layout = view.findViewById(R.id.first_linearLayout);
        second_linear_layout = view.findViewById(R.id.second_linearLayout);
        third_linear_layout = view.findViewById(R.id.third_linearLayout);
        welcome_container.setAnimation(AnimationClass.addSlideFromTopAnimation());
        first_linear_layout.setAnimation(AnimationClass.addSlideFromRightAnimation());
        second_linear_layout.setAnimation(AnimationClass.addSlideFromLeftAnimation());
        third_linear_layout.setAnimation(AnimationClass.addSlideFromRightAnimation());

        int currentDay = DateUtils.getCurrentDay();
        List<Integer> dates = DateUtils.getDatesForWeek(currentDay - (currentDay % 7));
        ViewUtil.setImageViewsClickable(view,R.id.first_linearLayout,R.id.second_linearLayout,R.id.third_linearLayout);
        return view;
    }
}