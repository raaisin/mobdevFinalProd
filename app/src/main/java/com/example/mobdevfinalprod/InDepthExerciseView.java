package com.example.mobdevfinalprod;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobdevfinalprod.helperclasses.AnimationClass;
import com.example.mobdevfinalprod.helperclasses.DatabaseOperations;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class InDepthExerciseView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_in_depth_exercise_view);
        findViewById(R.id.exercise_description).setVisibility(View.GONE);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Intent intent = getIntent();
        String exerciseName = intent.getStringExtra("exerciseName");
        TextView textView = findViewById(R.id.exerciseName);
        textView.setText(exerciseName);

        if (exerciseName != null) {
            exerciseName = exerciseName.replaceAll(" ", "").toLowerCase();
        }

        setExerciseImage(exerciseName);
        setExerciseDescription(exerciseName);
//        searchExercise(exerciseName);
        findViewById(R.id.return_button).setOnClickListener(v -> finish());
    }
    private void searchExercise(String exerciseName, String exerciseDescription) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference exerciseReference = database.collection("exercise_descriptions").document(exerciseName);

        database.runTransaction((Transaction.Function<Void>) transaction -> {
            DocumentSnapshot snapshot = transaction.get(exerciseReference);
            if (snapshot.exists()) {
                runOnUiThread(() -> {
                    TextView descriptionView = findViewById(R.id.exercise_description);
                    descriptionView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                    descriptionView.setText(snapshot.get("description").toString());
                    findViewById(R.id.text_loading_indicator).setVisibility(View.GONE);
                });
            } else {
                Map<String, String> exercise = new HashMap<>();
                exercise.put("exerciseName", exerciseName);
                exercise.put("description", exerciseDescription);
                transaction.set(exerciseReference, exercise);
            }
            return null;
        }).addOnSuccessListener(aVoid -> {
            // Transaction succeeded
        }).addOnFailureListener(e -> {
            runOnUiThread(() -> Toast.makeText(getApplicationContext(), "Failed to add exercise", Toast.LENGTH_LONG).show());
        });
    }


    private void setExerciseDescription(String exerciseName) {
        TextView descriptionView = findViewById(R.id.exercise_description);
        String prompt = exerciseName + "Give a description and how to do the exercise properly. Remove asterisks";
        AIHelperPage.getAIResponse(prompt, new AIHelperPage.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(() -> {
                    String answer = response;
                    answer = answer.replace("*", "");
                    descriptionView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                    descriptionView.setText(answer);
                    searchExercise(exerciseName, answer);
                    findViewById(R.id.exercise_description).setVisibility(View.VISIBLE);
                    descriptionView.startAnimation(AnimationClass.addFadeInAnimation());
                });
            }
        });
    }
    private void setExerciseImage(String exerciseName) {
        exerciseName = exerciseName.replace(" ","").toLowerCase();
        ImageView imageView = findViewById(R.id.exercise_image);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("exercise_images/"+exerciseName+".png");
        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }
}