package com.example.mazigrampanchayat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Activity for submitting suggestions and viewing user's suggestion history
 * Features: Form submission, validation, duplicate check, status tracking
 */
public class Activity_suggestionbox extends AppCompatActivity {

    // Views
    private ImageButton backButton;
    private MaterialButton newSuggestionButton, mySuggestionsButton;
    private FrameLayout formContainer, listContainer;
    
    // Form views
    private TextInputEditText nameEditText, mobileEditText, suggestionEditText;
    private AutoCompleteTextView categoryAutoComplete;
    private TextView characterCounter;
    private MaterialButton submitButton;
    private ProgressBar progressBar;
    
    // List views
    private RecyclerView mySuggestionsRecyclerView;
    private LinearLayout emptyStateLayout, loadingLayout;
    private ProgressBar listProgressBar;
    private TextView loadingMessage;
    
    // Data
    private String userMobileNo, userName;
    private SuggestionAdapter suggestionAdapter;
    private ArrayList<Suggestion> suggestionList;
    private DatabaseReference suggestionsRef;
    
    // Categories
    private String[] categories = {
        "पाणीपुरवठा",
        "रस्ते",
        "स्वच्छता",
        "शिक्षण",
        "आरोग्य",
        "इतर"
    };
    
    private boolean isFormView = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suggestionbox);

        initializeViews();
        loadUserData();
        setupCategoryDropdown();
        setupCharacterCounter();
        setupButtonListeners();
        setupSubmitButton();
        
        if (!isInternetAvailable()) {
            Toast.makeText(this, "इंटरनेट कनेक्शन उपलब्ध नाही", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        newSuggestionButton = findViewById(R.id.newSuggestionButton);
        mySuggestionsButton = findViewById(R.id.mySuggestionsButton);
        formContainer = findViewById(R.id.formContainer);
        listContainer = findViewById(R.id.listContainer);
        
        // Form views
        nameEditText = findViewById(R.id.nameEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        suggestionEditText = findViewById(R.id.suggestionEditText);
        categoryAutoComplete = findViewById(R.id.categoryAutoComplete);
        characterCounter = findViewById(R.id.characterCounter);
        submitButton = findViewById(R.id.submitButton);
        progressBar = findViewById(R.id.progressBar);
        
        // List views
        mySuggestionsRecyclerView = findViewById(R.id.mySuggestionsRecyclerView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        loadingLayout = findViewById(R.id.loadingLayout);
        listProgressBar = findViewById(R.id.progressBar);
        loadingMessage = findViewById(R.id.loadingMessage);
        
        suggestionList = new ArrayList<>();
        suggestionsRef = FirebaseDatabase.getInstance().getReference("suggestions");
    }

    /**
     * Load user data from SharedPreferences
     */
    private void loadUserData() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userMobileNo = preferences.getString("userMobileNo", "");
        
        // Load user name from Firebase
        DatabaseHelper databaseHelper = new DatabaseHelper();
        databaseHelper.databaseReference.orderByChild("mobileNo").equalTo(userMobileNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                                User user = userSnapshot.getValue(User.class);
                                if (user != null) {
                                    userName = user.getName();
                                    nameEditText.setText(userName);
                                    mobileEditText.setText(userMobileNo);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
    }

    /**
     * Setup category dropdown
     */
    private void setupCategoryDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            categories
        );
        categoryAutoComplete.setAdapter(adapter);
    }

    /**
     * Setup character counter for suggestion text
     */
    private void setupCharacterCounter() {
        suggestionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int remaining = 500 - s.length();
                characterCounter.setText(s.length() + " / 500");
                
                if (remaining < 50) {
                    characterCounter.setTextColor(getResources().getColor(R.color.error_red));
                } else {
                    characterCounter.setTextColor(getResources().getColor(R.color.text_secondary));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    /**
     * Setup button listeners for view switching
     */
    private void setupButtonListeners() {
        backButton.setOnClickListener(v -> finish());
        
        newSuggestionButton.setOnClickListener(v -> {
            showFormView();
        });
        
        mySuggestionsButton.setOnClickListener(v -> {
            showMySuggestionsView();
        });
    }

    /**
     * Show form view
     */
    private void showFormView() {
        isFormView = true;
        formContainer.setVisibility(View.VISIBLE);
        listContainer.setVisibility(View.GONE);
        newSuggestionButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.primary_blue));
        newSuggestionButton.setTextColor(getResources().getColor(R.color.white));
        mySuggestionsButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.light_gray));
        mySuggestionsButton.setTextColor(getResources().getColor(R.color.text_primary));
    }

    /**
     * Show my suggestions view
     */
    private void showMySuggestionsView() {
        isFormView = false;
        formContainer.setVisibility(View.GONE);
        listContainer.setVisibility(View.VISIBLE);
        mySuggestionsButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.primary_blue));
        mySuggestionsButton.setTextColor(getResources().getColor(R.color.white));
        newSuggestionButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.light_gray));
        newSuggestionButton.setTextColor(getResources().getColor(R.color.text_primary));
        
        // Load user's suggestions
        loadMySuggestions();
    }

    /**
     * Setup submit button
     */
    private void setupSubmitButton() {
        submitButton.setOnClickListener(v -> {
            if (validateForm()) {
                if (isInternetAvailable()) {
                    checkDuplicateAndSubmit();
                } else {
                    Toast.makeText(this, "इंटरनेट कनेक्शन उपलब्ध नाही", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Validate form inputs
     */
    private boolean validateForm() {
        String category = categoryAutoComplete.getText().toString().trim();
        String suggestion = suggestionEditText.getText().toString().trim();
        
        if (TextUtils.isEmpty(category)) {
            categoryAutoComplete.setError("कृपया श्रेणी निवडा");
            return false;
        }
        
        if (TextUtils.isEmpty(suggestion)) {
            suggestionEditText.setError("कृपया सूचना प्रविष्ट करा");
            return false;
        }
        
        if (suggestion.length() < 10) {
            suggestionEditText.setError("सूचना किमान 10 वर्णांची असावी");
            return false;
        }
        
        return true;
    }

    /**
     * Check for duplicate submissions and submit
     */
    private void checkDuplicateAndSubmit() {
        String suggestionText = suggestionEditText.getText().toString().trim();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        Date yesterday = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String yesterdayStr = dateFormat.format(yesterday);
        
        // Check for duplicates in last 24 hours
        suggestionsRef.orderByChild("userId").equalTo(userMobileNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isDuplicate = false;
                        for (DataSnapshot suggestionSnapshot : snapshot.getChildren()) {
                            Suggestion existingSuggestion = suggestionSnapshot.getValue(Suggestion.class);
                            if (existingSuggestion != null &&
                                existingSuggestion.getSuggestion().equals(suggestionText) &&
                                existingSuggestion.getSubmissionDate().compareTo(yesterdayStr) >= 0) {
                                isDuplicate = true;
                                break;
                            }
                        }
                        
                        if (isDuplicate) {
                            Toast.makeText(Activity_suggestionbox.this,
                                "ही सूचना आधीच सबमिट केलेली आहे", Toast.LENGTH_SHORT).show();
                        } else {
                            submitSuggestion();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Activity_suggestionbox.this,
                            "त्रुटी: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Submit suggestion to Firebase
     */
    private void submitSuggestion() {
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);
        
        String category = categoryAutoComplete.getText().toString().trim();
        String suggestionText = suggestionEditText.getText().toString().trim();
        
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
        
        String date = dateFormat.format(calendar.getTime());
        String time = timeFormat.format(calendar.getTime());
        
        Suggestion suggestion = new Suggestion(
            userMobileNo,
            userName,
            userMobileNo,
            category,
            suggestionText,
            date,
            time
        );
        
        String suggestionId = suggestionsRef.push().getKey();
        if (suggestionId != null) {
            suggestionsRef.child(suggestionId).setValue(suggestion)
                    .addOnSuccessListener(aVoid -> {
                        progressBar.setVisibility(View.GONE);
                        submitButton.setEnabled(true);
                        showSuccessDialog();
                        clearForm();
                    })
                    .addOnFailureListener(e -> {
                        progressBar.setVisibility(View.GONE);
                        submitButton.setEnabled(true);
                        Toast.makeText(Activity_suggestionbox.this,
                            "सबमिशन अयशस्वी: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * Show success dialog
     */
    private void showSuccessDialog() {
        new AlertDialog.Builder(this)
                .setTitle("यशस्वी")
                .setMessage("आपला सूचना/अभिप्राय यशस्वीरित्या सबमिट झाला आहे. धन्यवाद!")
                .setPositiveButton("ठीक आहे", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("आणखी सबमिट करा", (dialog, which) -> {
                    dialog.dismiss();
                    // Form is already cleared, user can submit another
                })
                .show();
    }

    /**
     * Clear form fields
     */
    private void clearForm() {
        categoryAutoComplete.setText("");
        suggestionEditText.setText("");
        characterCounter.setText("0 / 500");
    }

    /**
     * Load user's suggestions
     */
    private void loadMySuggestions() {
        showListLoading();
        
        suggestionsRef.orderByChild("userId").equalTo(userMobileNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        suggestionList.clear();
                        for (DataSnapshot suggestionSnapshot : snapshot.getChildren()) {
                            Suggestion suggestion = suggestionSnapshot.getValue(Suggestion.class);
                            if (suggestion != null) {
                                suggestion.setSuggestionId(suggestionSnapshot.getKey());
                                suggestionList.add(suggestion);
                            }
                        }
                        
                        // Sort by date (newest first)
                        suggestionList.sort((s1, s2) -> {
                            int dateCompare = s2.getSubmissionDate().compareTo(s1.getSubmissionDate());
                            if (dateCompare != 0) return dateCompare;
                            return s2.getSubmissionTime().compareTo(s1.getSubmissionTime());
                        });
                        
                        hideListLoading();
                        
                        if (suggestionList.isEmpty()) {
                            showListEmptyState();
                        } else {
                            hideListEmptyState();
                            setupRecyclerView();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        hideListLoading();
                        Toast.makeText(Activity_suggestionbox.this,
                            "सूचना लोड करण्यात अडचण: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Setup RecyclerView for suggestions list
     */
    private void setupRecyclerView() {
        if (suggestionAdapter == null) {
            suggestionAdapter = new SuggestionAdapter(suggestionList);
            mySuggestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mySuggestionsRecyclerView.setAdapter(suggestionAdapter);
        } else {
            suggestionAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Show loading state for list
     */
    private void showListLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        mySuggestionsRecyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    /**
     * Hide loading state for list
     */
    private void hideListLoading() {
        loadingLayout.setVisibility(View.GONE);
        mySuggestionsRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show empty state for list
     */
    private void showListEmptyState() {
        emptyStateLayout.setVisibility(View.VISIBLE);
        mySuggestionsRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
    }

    /**
     * Hide empty state for list
     */
    private void hideListEmptyState() {
        emptyStateLayout.setVisibility(View.GONE);
        mySuggestionsRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Check internet connectivity
     */
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }
        return false;
    }
}

