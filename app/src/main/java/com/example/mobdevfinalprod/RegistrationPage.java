package com.example.mobdevfinalprod;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationPage extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FirebaseFirestore database;

    public RegistrationPage() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationPage.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationPage newInstance(String param1, String param2) {
        RegistrationPage fragment = new RegistrationPage();
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
        View view = inflater.inflate(R.layout.fragment_registration_page, container, false);
        view.findViewById(R.id.register_loading).setVisibility(View.GONE);
        view.findViewById(R.id.to_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                WelcomePage.changeToLogin(fragmentManager);
            }
        });

        view.findViewById(R.id.register_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView textView = view.findViewById(R.id.register_message);
                String username = ((EditText)view.findViewById(R.id.username_register)).getText().toString();
                String password = ((EditText)view.findViewById(R.id.password_regsister)).getText().toString();
                String confirm_password = ((EditText)view.findViewById(R.id.confirm_password_register)).getText().toString();

                if (username.isEmpty() || password.isEmpty()){
                    textView.setText("All fields are required");
                    textView.setTextColor(Color.RED);
                    return;
                }
                if(!password.equals(confirm_password)) {
                    textView.setText("Password does not match");
                    textView.setTextColor(Color.RED);
                }
                else{
                    view.findViewById(R.id.register_loading).setVisibility(View.VISIBLE);
                    view.findViewById(R.id.register_text).setVisibility(View.GONE);
                    database = FirebaseFirestore.getInstance();
                    Map<String,String> users = new HashMap<>();
                    users.put("username",username);
                    users.put("password",password);

                    DocumentReference userReference = database.collection("users").document(username);

                    database.runTransaction((Transaction.Function<Void>) transaction -> {
                        DocumentSnapshot snapshot = transaction.get(userReference);
                        if (snapshot.exists()) {
                            textView.setText("Username is already existing");
                            textView.setTextColor(Color.RED);
                            view.findViewById(R.id.register_loading).setVisibility(View.GONE);
                            view.findViewById(R.id.register_text).setVisibility(View.VISIBLE);
                            throw new FirebaseFirestoreException("User already exist", FirebaseFirestoreException.Code.ALREADY_EXISTS);
                        }
                        else {
                            transaction.set(userReference, users);
                        }
                        return null;
                    }).addOnSuccessListener(aVoid -> {
                        textView.setText("Registration successful");
                        textView.setTextColor(Color.GREEN);
                        view.findViewById(R.id.register_loading).setVisibility(View.GONE);
                        view.findViewById(R.id.register_text).setVisibility(View.VISIBLE);
                    }).addOnFailureListener(e -> {
                        textView.setText("User already exist");
                        textView.setTextColor(Color.RED);
                        view.findViewById(R.id.register_loading).setVisibility(View.GONE);
                        view.findViewById(R.id.register_text).setVisibility(View.VISIBLE);
                    });
                }
            }
        });
        return view;
    }
}