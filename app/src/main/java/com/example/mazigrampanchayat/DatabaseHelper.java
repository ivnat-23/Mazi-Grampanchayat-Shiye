package com.example.mazigrampanchayat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DatabaseHelper {
    DatabaseReference databaseReference;

    public DatabaseHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("Users");
    }

    public void addUser(String userId, User user, DatabaseCallback callback) {
        databaseReference.child(userId).setValue(user)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(e -> callback.onFailure(e));
    }

    public interface DatabaseCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}
