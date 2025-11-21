package com.example.mazigrampanchayat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;

public class Activity_login extends AppCompatActivity {
    private EditText mobNo, password;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView forgotPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mobNo = findViewById(R.id.mob_no);
        password = findViewById(R.id.Password);
        loginButton = findViewById(R.id.login);
        progressBar = findViewById(R.id.ProgressBar);
        forgotPassword = findViewById(R.id.forgotPassword);


        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            Intent intent = new Intent(Activity_login.this, Activity_home.class);
            startActivity(intent);
            finish();
        }

        DatabaseHelper databaseHelper = new DatabaseHelper();

        loginButton.setOnClickListener(view -> {
            String mobileNo = mobNo.getText().toString().trim();
            String pass = password.getText().toString().trim();

            if (TextUtils.isEmpty(mobileNo)) {
                mobNo.setError("मोबाइल नंबर आवश्यक आहे");
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                password.setError("पासवर्ड आवश्यक आहे");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            databaseHelper.databaseReference.orderByChild("mobileNo").equalTo(mobileNo)
                    .get().addOnCompleteListener(task -> {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful() && task.getResult() != null && task.getResult().exists()) {
                            for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null && user.getPassword().equals(pass)) {
                                    Toast.makeText(Activity_login.this, "लॉगिन यशस्वी", Toast.LENGTH_SHORT).show();

                                    // Save login state and user mobile number in SharedPreferences
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.putString("userMobileNo", mobileNo);
                                    editor.apply();

                                    // Proceed to the next activity
                                    Intent intent = new Intent(Activity_login.this, Activity_home.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Activity_login.this, "चुकीचा पासवर्ड", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(Activity_login.this, "वापरकर्ता आढळला नाही", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Activity_login.this, "त्रुटी: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });

        forgotPassword.setOnClickListener(view -> {
            Toast.makeText(Activity_login.this, "पासवर्ड विसरलात? लवकरच जोडले जाईल", Toast.LENGTH_SHORT).show();
        });
    }
}
