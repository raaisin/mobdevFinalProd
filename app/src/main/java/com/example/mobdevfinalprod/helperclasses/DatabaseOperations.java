package com.example.mobdevfinalprod.helperclasses;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseOperations {
    public static void insertDataToDatabase(String username, Map<String,Object> data) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("customized_exercise").document(username);

        reference.get().addOnCompleteListener(task -> {
           if(task.isSuccessful()) {
               DocumentSnapshot document = task.getResult();
               if(document.exists()) {
                   reference.update(data);
               }
               else {
                   reference.set(data);
               }
           }
        });
    }
    public static void deleteUserDocument(String username) {
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference reference = database.collection("customized_exercise").document(username);
        reference.delete();
    }
}
