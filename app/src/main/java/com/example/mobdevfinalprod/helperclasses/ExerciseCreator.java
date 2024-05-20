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
        Collections.addAll(exercises, "armcircle", "armstretch",
                "crunch", "jumpingjack", "lunges", "mountainclimber", "squat", "stepup",
                "tricepstretch", "pushup", "plank", "highknees", "burpee", "buttkick",
                "bicyclecrunch", "flutterkick", "inchworm", "sideplank", "superman", "wallSit");
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
                    exerciseMap.put("name", exercise);
                    transaction.set(exerciseReference, exerciseMap);
                }
                return null;
            }).addOnSuccessListener(aVoid -> {
                Toast.makeText(context, "Exercise added to database", Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(context, "Exercise not added to database", Toast.LENGTH_SHORT).show();
            });
        }
    }
}
