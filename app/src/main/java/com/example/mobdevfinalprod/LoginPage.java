package com.example.mobdevfinalprod;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.AnimationClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView login_status;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore database;
    public LoginPage() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginPage.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginPage newInstance(String param1, String param2) {
        LoginPage fragment = new LoginPage();
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
        View view = inflater.inflate(R.layout.fragment_login_page, container, false);
        login_status = view.findViewById(R.id.login_status);
        view.findViewById(R.id.login_loading).setVisibility(View.GONE);
        view.findViewById(R.id.to_registration_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WelcomePage.changeToRegistration(fragmentManager);
            }
        });

        view.findViewById(R.id.login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = ((EditText) view.findViewById(R.id.username_login)).getText().toString();
                String password = ((EditText) view.findViewById(R.id.password_login)).getText().toString();
                if(username.isEmpty() || password.isEmpty()){
                    login_status.setText("All fields are required.");
                    login_status.setTextColor(Color.RED);
                    return;
                }
                view.findViewById(R.id.login_loading).setVisibility(View.VISIBLE);
                view.findViewById(R.id.login_text).setVisibility(View.GONE);

                //find if user already registerd
                database = FirebaseFirestore.getInstance();
                DocumentReference userReference = database.collection("users").document(username);

                userReference.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        if (snapshot.exists()) {
                            String storedPassword = snapshot.getString("password");
                            if (storedPassword.equals(password)) {
                                login_status.setText("Login Successful");
                                login_status.setTextColor(Color.GREEN);

                                //redirect sa main page and pass the username sa intent
                                Intent intent = new Intent(getContext(),MainPage.class);
                                intent.putExtra("username",username);
                                view.findViewById(R.id.login_loading).setVisibility(View.GONE);
                                view.findViewById(R.id.login_text).setVisibility(View.VISIBLE);
                                ((EditText) view.findViewById(R.id.username_login)).setText("");
                                ((EditText) view.findViewById(R.id.password_login)).setText("");
                                view.setAnimation(AnimationClass.addFadeOutAnimation());
                                startActivity(intent);
                            } else {
                                login_status.setText("Wrong password");
                                login_status.setTextColor(Color.RED);
                                view.findViewById(R.id.login_loading).setVisibility(View.GONE);
                                view.findViewById(R.id.login_text).setVisibility(View.VISIBLE);
                            }
                        } else {
                            login_status.setText("Username does not exist");
                            login_status.setTextColor(Color.RED);
                            view.findViewById(R.id.login_loading).setVisibility(View.GONE);
                            view.findViewById(R.id.login_text).setVisibility(View.VISIBLE);
                        }
                    }
                });
            }
        });
        return view;
    }
}