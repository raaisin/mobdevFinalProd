package com.example.mobdevfinalprod;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.AnimationClass;
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
import java.util.Objects;

public class DiscoverPage extends Fragment {

    private static final String ARG_USERNAME = "username";
    protected static List<String> exercises;
    private LinearLayout confirm_container;
    private boolean isConfirmed;
    public static Map<String, Object> data;
    public static int counter;
    private String username;
    private ProgressBar loadingIndicator;
    TextView createText;
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
        ImageButton createRoutineButton = view.findViewById(R.id.create_routine_button);
        ImageButton confirmButton = view.findViewById(R.id.confirm_button);
        ImageButton cancelButton = view.findViewById(R.id.cancel_button);
        createText = view.findViewById(R.id.text_create);
        data = new HashMap<>();
        isConfirmed = false;
        counter = 1;
        loadingIndicator.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        loadAllExercises(view);
        confirm_container.setVisibility(View.GONE);

        searchCustomExerciseDatabase(username, exists -> {
            if(exists){
                displayAllCustomizedExercise(personalExercises, username, getContext());
                loadingIndicator.setVisibility(View.GONE);
            }
            else {
                noExerciseFound(personalExercises,"No customized exercises for " + username + " found on database.");
                loadingIndicator.setVisibility(View.GONE);
            }
        });

        createRoutineButton.setOnClickListener(v -> {
            confirm_container.setVisibility(View.VISIBLE);
            createRoutineButton.setVisibility(View.GONE);
            createText.setVisibility(View.GONE);
        });
        confirmButton.setOnClickListener(v -> {
            if(!isConfirmed) {
                confirm_container.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                personalExercises.setVisibility(View.GONE);
                createRoutineButton.setVisibility(View.VISIBLE);
                createText.setVisibility(View.VISIBLE);
                isConfirmed = true;
            }
            else {
                DatabaseOperations.deleteUserDocument(username);
                DatabaseOperations.insertDataToDatabase(username,"customized_exercise", data);
                confirm_container.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                displayAllCustomizedExercise(personalExercises,username, getContext());
                noExerciseFound(personalExercises,"Ready for your workout? Tap here to see your personalized exercises!");
            }
        });
        cancelButton.setOnClickListener(v -> {
            confirm_container.setVisibility(View.GONE);
            recyclerView.setVisibility(View.GONE);
            personalExercises.setVisibility(View.VISIBLE);
            createRoutineButton.setVisibility(View.VISIBLE);
            createText.setVisibility(View.VISIBLE);
        });

        return view;
    }
    private void restartActivity(Context context, String username) {
        Intent intent = new Intent(context, MainPage.class);
        intent.putExtra("username", username);
        startActivity(intent);
        assert getActivity() != null;
        getActivity().finish();
    }

    public static void displayAllCustomizedExercise(LinearLayout personalExercises, String username, Context context) {
        personalExercises.removeAllViews();
        personalExercises.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("customized_exercise").document(username);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Map<String,Object> data = documentSnapshot.getData();
                    assert data != null;
                    for(final String exerciseName: data.keySet()) {
                        String exerciseNamesForReal = String.valueOf(data.get(exerciseName));
                        LinearLayout exerciseLayout = new LinearLayout(context);
                        ProgressBar loading = new ProgressBar(context);
                        loading.getIndeterminateDrawable().setColorFilter(Color.parseColor("#EE862B"), android.graphics.PorterDuff.Mode.SRC_IN);
                        exerciseLayout.addView(loading);
                        exerciseLayout.setOrientation(LinearLayout.VERTICAL);
                        String exerciseDatabaseName = exerciseNamesForReal.replace(" ","").toLowerCase();

                        StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                                .child("exercise_images/"+exerciseDatabaseName+".png");
                        final long ONE_MEGABYTE  = 1024 * 1024;

                        TextView exerciseNameTextView = new TextView(context);
                        setTextViewLayout(exerciseNameTextView, exerciseNamesForReal, context);
                        exerciseLayout.addView(exerciseNameTextView);
                        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                ImageView imageView = new ImageView(context);
                                setImageViewLayout(imageView, bitmap, exerciseLayout);
                                exerciseLayout.removeView(loading);
                                exerciseLayout.addView(imageView);
                                exerciseLayout.setBackgroundResource(R.drawable.exercise_layout_background);

                                imageView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(context, InDepthExerciseView.class);
                                        intent.putExtra("exerciseName", exerciseNamesForReal);
                                        context.startActivity(intent);
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
        Typeface font = ResourcesCompat.getFont(requireContext(), R.font.poppins);
        createNew.setTypeface(font);
        personalExercises.addView(createNew);
        createNew.setOnClickListener(click->restartActivity(getContext(),username));
    }
    private void searchCustomExerciseDatabase(String username, BooleanCallback callback) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("customized_exercise").document(username);
        reference.get().addOnCompleteListener(task->{
            if(task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                callback.onResponse(document.exists());
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
    private static void setImageViewLayout(ImageView imageView, Bitmap bitmap, LinearLayout layout) {
        imageView.setImageBitmap(bitmap);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 0, 50);
        layout.setLayoutParams(params);
        layout.setPadding(30,30,30,30);
    }
    private static void setTextViewLayout(TextView exerciseNameTextView, String exerciseNamesForReal, Context context) {
        exerciseNameTextView.setText(exerciseNamesForReal);
        exerciseNameTextView.setTextColor(ContextCompat.getColor(context, R.color.secondary_color));
        exerciseNameTextView.setTextSize(20);
        exerciseNameTextView.setGravity(Gravity.CENTER_HORIZONTAL);
        Typeface font = ResourcesCompat.getFont(context, R.font.poppins_medium);
        exerciseNameTextView.setTypeface(font);
    }

    public interface BooleanCallback {
        void onResponse(boolean exists);
    }

}