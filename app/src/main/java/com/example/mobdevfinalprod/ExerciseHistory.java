package com.example.mobdevfinalprod;

import android.graphics.text.LineBreaker;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.ViewUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExerciseHistory extends Fragment {
    private static final String ARG_USERNAME = "username";
    private static String username;
    private EditText historyInput;
    private ImageButton addHistoryButton;
    private static ProgressBar submit_loading;
    private static ImageView submit_button;
    private static final FirebaseFirestore database = FirebaseFirestore.getInstance();
    private static LinearLayout historyLayout;
    private static TextView month_container;
    SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
    String currentMonth = monthFormat.format(new Date());
    public ExerciseHistory() {
        // Required empty public constructor
    }
    public static ExerciseHistory newInstance(String param1) {
        ExerciseHistory fragment = new ExerciseHistory();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, param1);
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_history, container, false);
        addHistoryButton = view.findViewById(R.id.submit_log);
        historyInput = view.findViewById(R.id.log_message);
        submit_loading = view.findViewById(R.id.log_loading);
        submit_loading.setVisibility(View.GONE);
        submit_button = view.findViewById(R.id.submit_image);
        historyLayout = view.findViewById(R.id.log_list);
        month_container = view.findViewById(R.id.momth_name);
        month_container.setText(currentMonth);
        addToHistoryButtonFunction(addHistoryButton,historyInput);
        displayLogs(historyLayout);
        return view;
    }
    private void displayLogs(LinearLayout layout) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
        DocumentReference reference = database.collection("user_logs").document(username);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    layout.removeAllViews();
                    Map<String, Object> data = documentSnapshot.getData();
                    assert data != null;
                    for (Map.Entry<String, Object> entry : data.entrySet()) {
                        String date = entry.getKey();
                        String exercise = (String) entry.getValue();
                        try {
                            Date stringDate = dateFormat.parse(date);
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(stringDate);
                            SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM");
                            String monthString = monthFormat.format(cal.getTime()); // Use cal.getTime() instead of date

                            if(monthString.equals(currentMonth)) {
                                LinearLayout layout1 = new LinearLayout(getContext());
                                layout1.setPadding(40,40,40,40);
                                layout1.setBackgroundResource(R.drawable.log_background);
                                layout1.setOrientation(LinearLayout.VERTICAL);
                                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                );
                                layoutParams.setMargins(0,20,0,20);
                                layout1.setLayoutParams(layoutParams);

                                TextView dateView = new TextView(getContext());
                                dateView.setTextColor(ContextCompat.getColor(getContext(), R.color.secondary_color));
                                dateView.setText(date);

                                TextView logView = new TextView(getContext());
                                logView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                                logView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                                logView.setText(exercise);
                                layout1.addView(dateView);
                                layout1.addView(logView);
                                layout.addView(layout1);
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        });
    }

    private void addToHistoryButtonFunction(ImageButton addHistoryButton, EditText historyInput) {
        addHistoryButton.setOnClickListener(click -> {
            if (historyInput.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Please enter your exercise experience", Toast.LENGTH_SHORT).show();
                return;
            }
            ViewUtil.hideKeyboard(getActivity());
            submit_loading.setVisibility(View.VISIBLE);
            submit_button.setVisibility(View.GONE);
            Map<String, Object> data = new HashMap<>();
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
            Date currentDate = new Date();
            String formattedDate = sdf.format(currentDate);
            data.put(formattedDate, historyInput.getText().toString());

            addHistoryLog(data, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getContext(), "Log added successfully", Toast.LENGTH_LONG).show();
                        historyInput.setText("");
                    } else {
                        Toast.makeText(getContext(), "Failed to add log", Toast.LENGTH_LONG).show();
                    }
                    submit_loading.setVisibility(View.GONE);
                    submit_button.setVisibility(View.VISIBLE);
                    displayLogs(historyLayout);
                }
            });
        });
    }
    private static void addHistoryLog(Map<String, Object> data, OnCompleteListener<Void> onCompleteListener) {
        DocumentReference documentReference = database.collection("user_logs").document(username);

        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    Task<Void> updateTask;
                    if (document.exists()) {
                        updateTask = documentReference.update(data);
                    } else {
                        updateTask = documentReference.set(data);
                    }
                    updateTask.addOnCompleteListener(onCompleteListener);
                }
            }
        });
    }
}