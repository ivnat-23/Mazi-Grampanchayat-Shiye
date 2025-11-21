package com.example.mazigrampanchayat;

import android.app.DatePickerDialog;
import android.content.Context;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Activity_profile extends AppCompatActivity {
    private TextView tvName, tvMobileNo, tvBirthDate, tvNameHeader;
    private Button btnChangePassword, btnLogout;
    private ProgressBar progressBar;
    private DatabaseHelper databaseHelper;
    private User currentUser;
    private String userMobileNo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        tvName = findViewById(R.id.tvName);
        tvMobileNo = findViewById(R.id.tvMobileNo);
        tvBirthDate = findViewById(R.id.tvBirthDate);
        tvNameHeader = findViewById(R.id.tvNameHeader);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnLogout = findViewById(R.id.btnLogout);
        progressBar = findViewById(R.id.progressBar);

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        databaseHelper = new DatabaseHelper();

        // Get logged-in user's mobile number from SharedPreferences
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userMobileNo = preferences.getString("userMobileNo", "");

        if (TextUtils.isEmpty(userMobileNo)) {
            Toast.makeText(this, "वापरकर्ता माहिती आढळली नाही", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load user details
        loadUserDetails();

        // Change password button click
        btnChangePassword.setOnClickListener(v -> showChangePasswordDialog());

        // Logout button click
        btnLogout.setOnClickListener(v -> logoutUser());
    }

    private void loadUserDetails() {
        progressBar.setVisibility(View.VISIBLE);
        databaseHelper.databaseReference.orderByChild("mobileNo").equalTo(userMobileNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        progressBar.setVisibility(View.GONE);
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                currentUser = snapshot.getValue(User.class);
                                if (currentUser != null) {
                                    tvName.setText(currentUser.getName());
                                    tvNameHeader.setText(currentUser.getName());
                                    tvMobileNo.setText(currentUser.getMobileNo());
                                    tvBirthDate.setText(currentUser.getBirthDate());
                                }
                            }
                        } else {
                            Toast.makeText(Activity_profile.this, "वापरकर्ता आढळला नाही", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Activity_profile.this, "त्रुटी: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_change_password, null);
        builder.setView(dialogView);
        builder.setTitle("पासवर्ड बदला");

        EditText etNewPassword = dialogView.findViewById(R.id.etNewPassword);
        EditText etConfirmPassword = dialogView.findViewById(R.id.etConfirmPassword);
        EditText etOldPassword = dialogView.findViewById(R.id.etOldPassword);
        EditText etDateOfBirth = dialogView.findViewById(R.id.etDateOfBirth);
        Button btnUseOldPassword = dialogView.findViewById(R.id.btnUseOldPassword);
        Button btnUseDateOfBirth = dialogView.findViewById(R.id.btnUseDateOfBirth);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmit);

        AlertDialog dialog = builder.create();

        // Initially show old password option
        etOldPassword.setVisibility(View.VISIBLE);
        etDateOfBirth.setVisibility(View.GONE);
        btnUseOldPassword.setBackgroundResource(R.drawable.modern_button_blue);
        btnUseOldPassword.setTextColor(ContextCompat.getColor(this, android.R.color.white));
        btnUseDateOfBirth.setBackgroundResource(R.drawable.modern_button_outline);
        btnUseDateOfBirth.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));

        btnUseOldPassword.setOnClickListener(v -> {
            etOldPassword.setVisibility(View.VISIBLE);
            etDateOfBirth.setVisibility(View.GONE);
            etDateOfBirth.setText("");
            btnUseOldPassword.setBackgroundResource(R.drawable.modern_button_blue);
            btnUseOldPassword.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            btnUseDateOfBirth.setBackgroundResource(R.drawable.modern_button_outline);
            btnUseDateOfBirth.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
        });

        btnUseDateOfBirth.setOnClickListener(v -> {
            etOldPassword.setVisibility(View.GONE);
            etDateOfBirth.setVisibility(View.VISIBLE);
            etOldPassword.setText("");
            btnUseOldPassword.setBackgroundResource(R.drawable.modern_button_outline);
            btnUseOldPassword.setTextColor(ContextCompat.getColor(this, R.color.primary_blue));
            btnUseDateOfBirth.setBackgroundResource(R.drawable.modern_button_blue);
            btnUseDateOfBirth.setTextColor(ContextCompat.getColor(this, android.R.color.white));
            
            // Show date picker
            showDatePickerDialog(etDateOfBirth);
        });

        // Allow clicking on date field to open date picker
        etDateOfBirth.setOnClickListener(v -> {
            if (etDateOfBirth.getVisibility() == View.VISIBLE) {
                showDatePickerDialog(etDateOfBirth);
            }
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();

            if (TextUtils.isEmpty(newPassword)) {
                etNewPassword.setError("नवीन पासवर्ड आवश्यक आहे");
                return;
            }

            if (newPassword.length() < 6) {
                etNewPassword.setError("पासवर्ड किमान 6 वर्णांचा असावा");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                etConfirmPassword.setError("पासवर्ड जुळत नाहीत");
                return;
            }

            // Verify authentication method
            if (etOldPassword.getVisibility() == View.VISIBLE) {
                // Verify with old password
                String oldPassword = etOldPassword.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword)) {
                    etOldPassword.setError("जुना पासवर्ड आवश्यक आहे");
                    return;
                }

                if (currentUser != null && currentUser.getPassword().equals(oldPassword)) {
                    updatePassword(newPassword, dialog);
                } else {
                    etOldPassword.setError("चुकीचा पासवर्ड");
                }
            } else {
                // Verify with date of birth
                String dateOfBirth = etDateOfBirth.getText().toString().trim();
                if (TextUtils.isEmpty(dateOfBirth)) {
                    etDateOfBirth.setError("जन्म तारीख आवश्यक आहे");
                    return;
                }

                if (currentUser != null && currentUser.getBirthDate().equals(dateOfBirth)) {
                    updatePassword(newPassword, dialog);
                } else {
                    etDateOfBirth.setError("चुकीची जन्म तारीख");
                }
            }
        });

        dialog.show();
    }

    private void showDatePickerDialog(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String birthDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    editText.setText(birthDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void updatePassword(String newPassword, AlertDialog dialog) {
        progressBar.setVisibility(View.VISIBLE);
        
        databaseHelper.databaseReference.orderByChild("mobileNo").equalTo(userMobileNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String userId = snapshot.getKey();
                                if (userId != null) {
                                    currentUser.setPassword(newPassword);
                                    databaseHelper.databaseReference.child(userId).setValue(currentUser)
                                            .addOnSuccessListener(aVoid -> {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Activity_profile.this, "पासवर्ड यशस्वीरित्या बदलला", Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                            })
                                            .addOnFailureListener(e -> {
                                                progressBar.setVisibility(View.GONE);
                                                Toast.makeText(Activity_profile.this, "त्रुटी: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                            });
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Activity_profile.this, "त्रुटी: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void logoutUser() {
        new AlertDialog.Builder(this)
                .setTitle("लॉगआउट")
                .setMessage("तुम्हाला खात्री आहे की तुम्ही लॉगआउट करू इच्छिता?")
                .setPositiveButton("होय", (dialog, which) -> {
                    SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(Activity_profile.this, Activity_welcome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("नाही", null)
                .show();
    }
}

