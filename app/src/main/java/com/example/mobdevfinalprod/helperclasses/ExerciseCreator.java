package com.example.mobdevfinalprod.helperclasses;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Transaction;

import java.util.*;

public class ExerciseCreator {
    public static List<String> createExercises(){
        List<String> exercises = new ArrayList<>();
        Collections.addAll(exercises,  "Arm Circle", "Arm Stretch", "Crunch",
                "Jumping Jack", "Lunges",
                "Mountain Climber", "Squat", "Step Up", "Tricep Stretch", "Push Up",
                "Plank", "High Knees", "Burpee", "Butt Kick", "Bicycle Crunch",
                "Flutter Kick", "Inchworm", "Side Plank", "Superman", "Wall Sit");
        return exercises;
    }
    public static void addExerciseToDatabase(List<String> exercises, Context context) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();

        for (String exercise : exercises) {
            DocumentReference exerciseReference = database.collection("exercise_all").document(exercise);

            database.runTransaction((Transaction.Function<Void>) transaction -> {
                DocumentSnapshot snapshot = transaction.get(exerciseReference);

                if (!snapshot.exists()) {
                    Map<String, String> exerciseMap = new HashMap<>();
                    String exercise_name = exercise.replace(" ","").toLowerCase();
                    exerciseMap.put("name", exercise_name);
                    transaction.set(exerciseReference, exerciseMap);
                }
                return null;
            }).addOnSuccessListener(aVoid -> {
//                Toast.makeText(context, "Exercise added to database", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
//                Toast.makeText(context, "Exercise not added to database", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
