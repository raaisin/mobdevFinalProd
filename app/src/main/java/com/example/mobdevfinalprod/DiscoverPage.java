package com.example.mobdevfinalprod;

import android.app.Activity;
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

import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mobdevfinalprod.helperclasses.DatabaseOperations;
import com.example.mobdevfinalprod.helperclasses.ExerciseCreator;
import com.example.mobdevfinalprod.helperclasses.MyAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.api.Distribution;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private String username;
    public DiscoverPage() {
        // Required empty public constructor
    }
    public DiscoverPage(String username) {
        this.username = username;
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

    protected static List<String> exercises;
    private ImageButton createRoutineButton;
    private LinearLayout confirm_container;
    private ImageButton confirmButton;
    private ImageButton cancelButton;
    private boolean isConfirmed;
    public static Map<String, Object> data;
    public static int counter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover_page, container, false);
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

        recyclerView.setVisibility(View.GONE);
        loadAllExercises(view);
        confirm_container.setVisibility(View.GONE);

        searchCustomExerciseDatabase(username, exists -> {
            if(exists){
                displayAllCustomizedExercise(personalExercises, username);
            }
            else {
                noExerciseFound(personalExercises);
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
                DatabaseOperations.insertDataToDatabase(username,data);
                confirm_container.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                displayAllCustomizedExercise(personalExercises, username);
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
    private void restartActivity() {
        Activity activity = getActivity();
        if (activity != null) {
            Intent intent = new Intent(activity, activity.getClass());
            activity.finish();
            startActivity(intent);
        }
    }
    private void displayAllCustomizedExercise(LinearLayout personalExercises, String username) {
        personalExercises.setVisibility(View.VISIBLE);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("customized_exercise").document(username);
        reference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()) {
                    Map<String,Object> data = documentSnapshot.getData();
                    int counter = 1;
                    for(final String exerciseName: data.keySet()) {
                        String fieldName = exerciseName;
                        String exerciseNamesForReal = String.valueOf(data.get(fieldName));
                        LinearLayout exerciseLayout = new LinearLayout(getContext());
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
    private void noExerciseFound(LinearLayout personalExercises) {
        TextView createNew = new TextView(getContext());
        createNew.setText("No customized exercises for " + username + " found on database.");
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