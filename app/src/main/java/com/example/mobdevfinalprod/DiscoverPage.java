package com.example.mobdevfinalprod;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.DatabaseOperations;
import com.example.mobdevfinalprod.helperclasses.ExerciseCreator;
import com.example.mobdevfinalprod.helperclasses.MyAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class DiscoverPage extends Fragment {

    private static final String ARG_USERNAME = "username";
    protected static List<String> exercises;
    private ImageButton createRoutineButton;
    private LinearLayout confirm_container;
    private ImageButton confirmButton;
    private ImageButton cancelButton;
    private boolean isConfirmed;
    public static Map<String, Object> data;
    public static int counter;
    private String username;
    private ProgressBar loadingIndicator;
    public DiscoverPage() {
        // Required empty public constructor
    }
    public static DiscoverPage newInstance(String param1) {
        DiscoverPage fragment = new DiscoverPage();
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

        View view = inflater.inflate(R.layout.fragment_discover_page, container, false);
        loadingIndicator = view.findViewById(R.id.loading_indicator);
        exercises = ExerciseCreator.createExercises();
        ExerciseCreator.addExerciseToDatabase(exercises,getContext());
        RecyclerView recyclerView = view.findViewById(R.id.exercise_list_recyclerView);
        LinearLayout personalExercises = view.findViewById(R.id.custom_exercise_container);
        confirm_container = view.findViewById(R.id.confirm_make_new_container);
        createRoutineButton = view.findViewById(R.id.create_routine_button);
        confirmButton = view.findViewById(R.id.confirm_button);
        cancelButton = view.findViewById(R.id.cancel_button);
        data = new HashMap<>();
        isConfirmed = false;
        counter = 1;
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        loadAllExercises(view);
        confirm_container.setVisibility(View.GONE);

        searchCustomExerciseDatabase(username, exists -> {
            if(exists){
                displayAllCustomizedExercise(personalExercises, username);
                loadingIndicator.setVisibility(View.GONE);
            }
            else {
                noExerciseFound(personalExercises,"No customized exercises for " + username + " found on database.");
                loadingIndicator.setVisibility(View.GONE);
            }
        });

        createRoutineButton.setOnClickListener(v -> {
            confirm_container.setVisibility(View.VISIBLE);
        });
        confirmButton.setOnClickListener(v -> {
            if(!isConfirmed) {
                confirm_container.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                personalExercises.setVisibility(View.GONE);
                isConfirmed = true;
            }
            else {
                DatabaseOperations.deleteUserDocument(username);
                DatabaseOperations.insertDataToDatabase(username, data);
                confirm_container.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                displayAllCustomizedExercise(personalExercises,username);
                noExerciseFound(personalExercises,"Refresh to see your customized exercise changes.");
                addRefresh(personalExercises);
            }
        });
        cancelButton.setOnClickListener(v -> {
            confirm_container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            confirm_container.setVisibility(View.GONE);
            personalExercises.setVisibility(View.VISIBLE);
        });

        return view;
    }
    private void addRefresh(LinearLayout personalExercises) {
        TextView refresh = new TextView(getContext());
        refresh.setText("Click here to Refresh");
        refresh.setPadding(50,0,0,0);
        refresh.setTextColor(Color.BLACK);
        refresh.setOnClickListener(click -> {
            restartActivity(getContext(),username);
        });
        personalExercises.addView(refresh);
    }
    private void restartActivity(Context context, String username) {
        Intent intent = new Intent(context, MainPage.class);
        intent.putExtra("username", username);
        startActivity(intent);
        getActivity().finish();
    }

    private void displayAllCustomizedExercise(LinearLayout personalExercises, String username) {
        personalExercises.removeAllViews();
        personalExercises.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("customized_exercise").document(username);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Map<String,Object> data = documentSnapshot.getData();
                    for(final String exerciseName: data.keySet()) {
                        String fieldName = exerciseName;
                        String exerciseNamesForReal = String.valueOf(data.get(fieldName));
                        LinearLayout exerciseLayout = new LinearLayout(getContext());
                        ProgressBar loading = new ProgressBar(getContext());
                        exerciseLayout.addView(loading);
                        exerciseLayout.setOrientation(LinearLayout.VERTICAL);
                        String exerciseDatabaseName = exerciseNamesForReal.replace(" ","").toLowerCase();

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                                .child("exercise_images/"+exerciseDatabaseName+".png");
                        final long ONE_MEGABYTE  = 1024 * 1024;

                        TextView exerciseNameTextView = new TextView(getContext());
                        setTextViewLayout(exerciseNameTextView, exerciseNamesForReal);
                        exerciseLayout.addView(exerciseNameTextView);
                        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ImageView imageView = new ImageView(getContext());
                                setImageViewLayout(imageView, bitmap, exerciseLayout);
                                exerciseLayout.removeView(loading);
                                exerciseLayout.addView(imageView);
                                exerciseLayout.setBackgroundResource(R.drawable.exercise_layout_background);

                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(getContext(), InDepthExerciseView.class);
                                        intent.putExtra("exerciseName", exerciseNamesForReal);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });
                        personalExercises.addView(exerciseLayout);
                        counter++;
                    }
                }
            }
        });
    }
    private void noExerciseFound(LinearLayout personalExercises, String message) {
        TextView createNew = new TextView(getContext());
        createNew.setText(message);
        createNew.setTextColor(Color.RED);
        createNew.setTextSize(14);
        createNew.setGravity(Gravity.CENTER_HORIZONTAL);
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.poppins);
        createNew.setTypeface(font);
        personalExercises.addView(createNew);
    }
    private void searchCustomExerciseDatabase(String username, BooleanCallback callback) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("customized_exercise").document(username);
        reference.get().addOnCompleteListener(task->{
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if(document.exists()) {
                    callback.onResponse(true);
                } else {
                    callback.onResponse(false);
                }
            }
            else{
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadAllExercises(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.exercise_list_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new MyAdapter(username,getContext(),exercises));
    }
    private void setImageViewLayout(ImageView imageView, Bitmap bitmap, LinearLayout layout) {
        imageView.setImageBitmap(bitmap);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 50);
        layout.setLayoutParams(params);
        layout.setPadding(30,30,30,30);
    }
    private void setTextViewLayout(TextView exerciseNameTextView, String exerciseNamesForReal) {
        exerciseNameTextView.setText(exerciseNamesForReal);
        exerciseNameTextView.setTextColor(Color.BLACK);
        exerciseNameTextView.setTextSize(20);
        exerciseNameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        Typeface font = ResourcesCompat.getFont(getContext(), R.font.poppins_medium);
        exerciseNameTextView.setTypeface(font);
    }

    public interface BooleanCallback {
        void onResponse(boolean exists);
    }

}