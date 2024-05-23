package com.example.mobdevfinalprod;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mobdevfinalprod.helperclasses.DateUtils;
import com.example.mobdevfinalprod.helperclasses.ViewUtil;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InitialView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InitialView extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    TextView username_container;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String username;

    public InitialView() {
        // Required empty public constructor
    }
    public InitialView(String username){
        this.username = username;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InitialView.
     */
    // TODO: Rename and change types and number of parameters
    public static InitialView newInstance(String param1, String param2) {
        InitialView fragment = new InitialView();
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
        View view = inflater.inflate(R.layout.fragment_initial_view, container, false);
        DateUtils.displayDates(view);
        username_container = view.findViewById(R.id.username_init);
        username_container.setText(username);

        ViewUtil.setImageViewsClickable(view,R.id.first_linearLayout,R.id.second_linearLayout,R.id.third_linearLayout);
        return view;
    }
}