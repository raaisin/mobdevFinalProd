package com.example.mobdevfinalprod;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.text.LineBreaker;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class InDepthExerciseView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_in_depth_exercise_view);
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
        TextView descriptionView = findViewById(R.id.exercise_description);
        String prompt = exerciseName + "Give a description and how to do the exercise properly. Remove asterisks";
        AIHelperPage.getAIResponse(prompt, new AIHelperPage.ResponseCallback() {
            @Override
            public void onResponse(String response) {
                runOnUiThread(()->{
                    String answer = response;
                    answer = answer.replace("*","");
                    descriptionView.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD);
                    descriptionView.setText(answer);
                });
            }
        });
        findViewById(R.id.return_button).setOnClickListener(v -> finish());
    }
}