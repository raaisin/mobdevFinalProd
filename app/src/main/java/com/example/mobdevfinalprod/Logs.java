package com.example.mobdevfinalprod;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Logs extends Fragment {

    private EditText editTextLog;
    private Button buttonSubmit;
    private FirebaseFirestore db;

    public Logs() {
        // Required empty public constructor
    }

    public static Logs newInstance(String param1) {
        Logs fragment = new Logs();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_logs, container, false);

        editTextLog = view.findViewById(R.id.editTextTextMultiLine);
        buttonSubmit = view.findViewById(R.id.button1);

        // Create an OnClickListener for the button
        View.OnClickListener buttonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String logText = editTextLog.getText().toString();
                if (!logText.isEmpty()) {
                    recordLog(logText);
                }
            }
        };

        // Set the OnClickListener for the button
        buttonSubmit.setOnClickListener(buttonClickListener);

        return view;
    }

    private void recordLog(String logText) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("log", logText);
        logData.put("timestamp", new Date());

        db.collection("user_logs")
                .add(logData)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        editTextLog.setText(""); // Clear the EditText field
                        Toast.makeText(getContext(), "Log saved successfully!", Toast.LENGTH_SHORT).show(); // Show a success message
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Error saving log: " + e.getMessage(), Toast.LENGTH_SHORT).show(); // Show an error message
                    }
                });
    }

}
