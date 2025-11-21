package com.example.mazigrampanchayat;
import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Activity_registration extends AppCompatActivity {
    private EditText idName, idMobileNo, idPassword, idRePassword;
    private Button registrationButton, startDatePickerButton;
    private TextView selectedDateTextView;
    private ProgressBar progressBar;

    private String birthDate = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        idName = findViewById(R.id.idname);
        idMobileNo = findViewById(R.id.idmobileno);
        idPassword = findViewById(R.id.idpassword);
        idRePassword = findViewById(R.id.idrepassword);
        startDatePickerButton = findViewById(R.id.startDatePickerButton);
        registrationButton = findViewById(R.id.registration);
        progressBar = findViewById(R.id.progressbar);

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());

        DatabaseHelper databaseHelper = new DatabaseHelper();

        startDatePickerButton.setOnClickListener(view -> showDatePickerDialog());

        registrationButton.setOnClickListener(view -> {
            String name = idName.getText().toString().trim();
            String mobileNo = idMobileNo.getText().toString().trim();
            String password = idPassword.getText().toString().trim();
            String rePassword = idRePassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                idName.setError("नाव आवश्यक आहे");
                return;
            }
            if (TextUtils.isEmpty(birthDate)) {
                Toast.makeText(this, "जन्म तारीख आवश्यक आहे", Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(mobileNo)) {
                idMobileNo.setError("मोबाइल नंबर आवश्यक आहे");
                return;
            }
            if (mobileNo.length() != 10) {
                idMobileNo.setError("मोबाइल नंबर 10 अंकांचा असावा");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                idPassword.setError("पासवर्ड आवश्यक आहे");
                return;
            }
            if (!password.equals(rePassword)) {
                idRePassword.setError("पासवर्ड जुळत नाहीत");
                return;
            }

            progressBar.setVisibility(View.VISIBLE);

            databaseHelper.databaseReference.orderByChild("mobileNo").equalTo(mobileNo)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(Activity_registration.this, "वापरकर्ता आधीच नोंदणीकृत आहे", Toast.LENGTH_SHORT).show();
                            } else {
                                String userId = databaseHelper.databaseReference.push().getKey();
                                User user = new User(name, birthDate, mobileNo, password);

                                databaseHelper.addUser(userId, user, new DatabaseHelper.DatabaseCallback() {
                                    @Override
                                    public void onSuccess() {
                                        progressBar.setVisibility(View.VISIBLE);

                                        new android.os.Handler().postDelayed(() -> {
                                            progressBar.setVisibility(View.GONE);
                                            Toast.makeText(Activity_registration.this, "वापरकर्ता यशस्वीरित्या नोंदणीकृत झाला", Toast.LENGTH_SHORT).show();

                                            Intent intent = new Intent(Activity_registration.this, Activity_login.class);
                                            startActivity(intent);
                                            finish();
                                        }, 2000);
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        progressBar.setVisibility(View.GONE);
                                        Toast.makeText(Activity_registration.this, "नोंदणी अयशस्वी: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(Activity_registration.this, "त्रुटी: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    birthDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    startDatePickerButton.setText(birthDate);
                }, year, month, day);

        datePickerDialog.show();
    }

    private void clearFields() {
        idName.setText("");
        idMobileNo.setText("");
        idPassword.setText("");
        idRePassword.setText("");
        startDatePickerButton.setText("जन्म तारीख");
        birthDate = "";
    }
}
