package com.example.mazigrampanchayat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
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
import java.util.Locale;

/**
 * Activity for Birth Certificate request form
 * Multi-step form with 5 steps: Child Info, Mother Info, Father Info, Address, Remarks
 */
public class Activity_birthcertificate extends AppCompatActivity {

    // Views
    private ImageButton backButton;
    private TextView stepIndicator;
    private ScrollView formScrollView;
    private MaterialButton previousButton, nextButton, submitButton;
    private MaterialButton newRequestButton, myCertificatesButton;
    private FrameLayout formContainer, certificatesContainer;
    private LinearLayout progressIndicator, navigationButtons;
    
    // Step layouts
    private LinearLayout step1Layout, step2Layout, step3Layout, step4Layout, step5Layout;
    
    // Step 1: Child Info
    private TextInputEditText childNameMarathi, childNameEnglish, dateOfBirth;
    private AutoCompleteTextView placeOfBirth;
    private RadioGroup sexRadioGroup;
    private RadioButton sexMale, sexFemale;
    
    // Step 2: Mother Info
    private TextInputEditText motherNameMarathi, motherNameEnglish, motherAadhaar;
    
    // Step 3: Father Info
    private TextInputEditText fatherNameMarathi, fatherNameEnglish, fatherAadhaar;
    
    // Step 4: Address
    private TextInputEditText birthAddress, permanentAddress;
    private CheckBox sameAsAboveCheckbox;
    private TextInputEditText taluka, district, state, pincode;
    
    // Step 5: Remarks
    private TextInputEditText remarks;
    
    // Data
    private int currentStep = 1;
    private String userMobileNo;
    private DatabaseReference birthCertRef;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private boolean isFormView = true;
    
    // My Certificates views
    private RecyclerView myCertificatesRecyclerView;
    private LinearLayout emptyStateLayout, loadingLayout;
    private ProgressBar listProgressBar;
    private TextView loadingMessage;
    private BirthCertificateAdapter certificateAdapter;
    private ArrayList<BirthCertificateRequest> certificateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_birthcertificate);

        initializeViews();
        setupDatePicker();
        setupPlaceOfBirthDropdown();
        setupCheckboxListener();
        setupButtonListeners();
        setupTabButtons();
        loadUserData();
        
        birthCertRef = FirebaseDatabase.getInstance().getReference("birth_certificate_requests");
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        
        certificateList = new ArrayList<>();
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        stepIndicator = findViewById(R.id.stepIndicator);
        formScrollView = findViewById(R.id.formScrollView);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        submitButton = findViewById(R.id.submitButton);
        newRequestButton = findViewById(R.id.newRequestButton);
        myCertificatesButton = findViewById(R.id.myCertificatesButton);
        formContainer = findViewById(R.id.formContainer);
        certificatesContainer = findViewById(R.id.certificatesContainer);
        progressIndicator = findViewById(R.id.progressIndicator);
        navigationButtons = findViewById(R.id.navigationButtons);
        
        // Step layouts
        step1Layout = findViewById(R.id.step1Layout);
        step2Layout = findViewById(R.id.step2Layout);
        step3Layout = findViewById(R.id.step3Layout);
        step4Layout = findViewById(R.id.step4Layout);
        step5Layout = findViewById(R.id.step5Layout);
        
        // Step 1
        childNameMarathi = findViewById(R.id.childNameMarathi);
        childNameEnglish = findViewById(R.id.childNameEnglish);
        dateOfBirth = findViewById(R.id.dateOfBirth);
        placeOfBirth = findViewById(R.id.placeOfBirth);
        sexRadioGroup = findViewById(R.id.sexRadioGroup);
        sexMale = findViewById(R.id.sexMale);
        sexFemale = findViewById(R.id.sexFemale);
        
        // Step 2
        motherNameMarathi = findViewById(R.id.motherNameMarathi);
        motherNameEnglish = findViewById(R.id.motherNameEnglish);
        motherAadhaar = findViewById(R.id.motherAadhaar);
        
        // Step 3
        fatherNameMarathi = findViewById(R.id.fatherNameMarathi);
        fatherNameEnglish = findViewById(R.id.fatherNameEnglish);
        fatherAadhaar = findViewById(R.id.fatherAadhaar);
        
        // Step 4
        birthAddress = findViewById(R.id.birthAddress);
        permanentAddress = findViewById(R.id.permanentAddress);
        sameAsAboveCheckbox = findViewById(R.id.sameAsAboveCheckbox);
        taluka = findViewById(R.id.taluka);
        district = findViewById(R.id.district);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        
        // Step 5
        remarks = findViewById(R.id.remarks);
        
        // My Certificates views
        myCertificatesRecyclerView = findViewById(R.id.myCertificatesRecyclerView);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        loadingLayout = findViewById(R.id.loadingLayout);
        listProgressBar = findViewById(R.id.progressBar);
        loadingMessage = findViewById(R.id.loadingMessage);
    }

    /**
     * Setup date picker for date of birth
     */
    private void setupDatePicker() {
        dateOfBirth.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    String selectedDate = dateFormat.format(calendar.getTime());
                    dateOfBirth.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            );
            // Set max date to today
            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            datePickerDialog.show();
        });
    }

    /**
     * Setup place of birth dropdown
     */
    private void setupPlaceOfBirthDropdown() {
        String[] places = {"ग्रामीण रुग्णालय शिये", "इतर"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_dropdown_item_1line,
            places
        );
        placeOfBirth.setAdapter(adapter);
    }

    /**
     * Setup checkbox listener for "Same as above"
     */
    private void setupCheckboxListener() {
        sameAsAboveCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                permanentAddress.setText(birthAddress.getText().toString());
                permanentAddress.setEnabled(false);
            } else {
                permanentAddress.setEnabled(true);
            }
        });
    }

    /**
     * Setup button listeners
     */
    private void setupButtonListeners() {
        backButton.setOnClickListener(v -> finish());
        previousButton.setOnClickListener(v -> goToPreviousStep());
        nextButton.setOnClickListener(v -> goToNextStep());
        submitButton.setOnClickListener(v -> submitRequest());
    }
    
    /**
     * Setup tab buttons for switching between form and certificates
     */
    private void setupTabButtons() {
        newRequestButton.setOnClickListener(v -> showFormView());
        myCertificatesButton.setOnClickListener(v -> showCertificatesView());
    }
    
    /**
     * Show form view
     */
    private void showFormView() {
        isFormView = true;
        formContainer.setVisibility(View.VISIBLE);
        certificatesContainer.setVisibility(View.GONE);
        progressIndicator.setVisibility(View.VISIBLE);
        navigationButtons.setVisibility(View.VISIBLE);
        
        newRequestButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.primary_blue));
        newRequestButton.setTextColor(getResources().getColor(R.color.white));
        myCertificatesButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.light_gray));
        myCertificatesButton.setTextColor(getResources().getColor(R.color.text_primary));
    }
    
    /**
     * Show certificates view
     */
    private void showCertificatesView() {
        isFormView = false;
        formContainer.setVisibility(View.GONE);
        certificatesContainer.setVisibility(View.VISIBLE);
        progressIndicator.setVisibility(View.GONE);
        navigationButtons.setVisibility(View.GONE);
        
        myCertificatesButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.primary_blue));
        myCertificatesButton.setTextColor(getResources().getColor(R.color.white));
        newRequestButton.setBackgroundTintList(
            getResources().getColorStateList(R.color.light_gray));
        newRequestButton.setTextColor(getResources().getColor(R.color.text_primary));
        
        // Load user's certificates
        loadMyCertificates();
    }

    /**
     * Load user data
     */
    private void loadUserData() {
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userMobileNo = preferences.getString("userMobileNo", "");
    }

    /**
     * Go to previous step
     */
    private void goToPreviousStep() {
        if (currentStep > 1) {
            currentStep--;
            showStep(currentStep);
        }
    }

    /**
     * Go to next step
     */
    private void goToNextStep() {
        if (validateCurrentStep()) {
            if (currentStep < 5) {
                currentStep++;
                showStep(currentStep);
            }
        }
    }

    /**
     * Show specific step
     */
    private void showStep(int step) {
        // Hide all steps
        step1Layout.setVisibility(View.GONE);
        step2Layout.setVisibility(View.GONE);
        step3Layout.setVisibility(View.GONE);
        step4Layout.setVisibility(View.GONE);
        step5Layout.setVisibility(View.GONE);
        
        // Show current step
        switch (step) {
            case 1:
                step1Layout.setVisibility(View.VISIBLE);
                previousButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);
                break;
            case 2:
                step2Layout.setVisibility(View.VISIBLE);
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);
                break;
            case 3:
                step3Layout.setVisibility(View.VISIBLE);
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);
                break;
            case 4:
                step4Layout.setVisibility(View.VISIBLE);
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.VISIBLE);
                submitButton.setVisibility(View.GONE);
                break;
            case 5:
                step5Layout.setVisibility(View.VISIBLE);
                previousButton.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
                submitButton.setVisibility(View.VISIBLE);
                break;
        }
        
        // Update step indicator
        stepIndicator.setText("पायरी " + step + " / 5");
        
        // Scroll to top
        formScrollView.post(() -> formScrollView.fullScroll(ScrollView.FOCUS_UP));
    }

    /**
     * Validate current step
     */
    private boolean validateCurrentStep() {
        switch (currentStep) {
            case 1:
                return validateStep1();
            case 2:
                return validateStep2();
            case 3:
                return validateStep3();
            case 4:
                return validateStep4();
            case 5:
                return true; // Remarks are optional
            default:
                return false;
        }
    }

    /**
     * Validate Step 1: Child Information
     */
    private boolean validateStep1() {
        if (TextUtils.isEmpty(childNameMarathi.getText())) {
            childNameMarathi.setError("कृपया मुलाचे नाव प्रविष्ट करा");
            return false;
        }
        if (TextUtils.isEmpty(childNameEnglish.getText())) {
            childNameEnglish.setError("कृपया मुलाचे नाव (इंग्रजी) प्रविष्ट करा");
            return false;
        }
        if (TextUtils.isEmpty(dateOfBirth.getText())) {
            dateOfBirth.setError("कृपया जन्म तारीख निवडा");
            return false;
        }
        if (TextUtils.isEmpty(placeOfBirth.getText())) {
            placeOfBirth.setError("कृपया जन्मस्थान निवडा");
            return false;
        }
        if (sexRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "कृपया लिंग निवडा", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * Validate Step 2: Mother Information
     */
    private boolean validateStep2() {
        if (TextUtils.isEmpty(motherNameMarathi.getText())) {
            motherNameMarathi.setError("कृपया आईचे नाव प्रविष्ट करा");
            return false;
        }
        if (TextUtils.isEmpty(motherNameEnglish.getText())) {
            motherNameEnglish.setError("कृपया आईचे नाव (इंग्रजी) प्रविष्ट करा");
            return false;
        }
        String aadhaar = motherAadhaar.getText().toString().trim();
        if (TextUtils.isEmpty(aadhaar)) {
            motherAadhaar.setError("कृपया आधार कार्ड नंबर प्रविष्ट करा");
            return false;
        }
        if (aadhaar.length() != 12) {
            motherAadhaar.setError("आधार कार्ड नंबर 12 अंकांचा असावा");
            return false;
        }
        return true;
    }

    /**
     * Validate Step 3: Father Information
     */
    private boolean validateStep3() {
        if (TextUtils.isEmpty(fatherNameMarathi.getText())) {
            fatherNameMarathi.setError("कृपया वडिलांचे नाव प्रविष्ट करा");
            return false;
        }
        if (TextUtils.isEmpty(fatherNameEnglish.getText())) {
            fatherNameEnglish.setError("कृपया वडिलांचे नाव (इंग्रजी) प्रविष्ट करा");
            return false;
        }
        String aadhaar = fatherAadhaar.getText().toString().trim();
        if (TextUtils.isEmpty(aadhaar)) {
            fatherAadhaar.setError("कृपया आधार कार्ड नंबर प्रविष्ट करा");
            return false;
        }
        if (aadhaar.length() != 12) {
            fatherAadhaar.setError("आधार कार्ड नंबर 12 अंकांचा असावा");
            return false;
        }
        return true;
    }

    /**
     * Validate Step 4: Address Information
     */
    private boolean validateStep4() {
        if (TextUtils.isEmpty(birthAddress.getText())) {
            birthAddress.setError("कृपया जन्म वेळीचा पत्ता प्रविष्ट करा");
            return false;
        }
        if (TextUtils.isEmpty(permanentAddress.getText())) {
            permanentAddress.setError("कृपया कायमचा पत्ता प्रविष्ट करा");
            return false;
        }
        return true;
    }

    /**
     * Submit birth certificate request
     */
    private void submitRequest() {
        if (!validateCurrentStep()) {
            return;
        }
        
        if (!isInternetAvailable()) {
            Toast.makeText(this, "इंटरनेट कनेक्शन उपलब्ध नाही", Toast.LENGTH_SHORT).show();
            return;
        }
        
        // Check for duplicate requests
        checkDuplicateAndSubmit();
    }

    /**
     * Check for duplicate requests and submit
     */
    private void checkDuplicateAndSubmit() {
        String childName = childNameMarathi.getText().toString().trim();
        String dob = dateOfBirth.getText().toString().trim();
        
        birthCertRef.orderByChild("userId").equalTo(userMobileNo)
                .addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        boolean isDuplicate = false;
                        for (com.google.firebase.database.DataSnapshot requestSnapshot : snapshot.getChildren()) {
                            BirthCertificateRequest existing = requestSnapshot.getValue(BirthCertificateRequest.class);
                            if (existing != null && existing.getChildInfo() != null) {
                                if (existing.getChildInfo().getNameMarathi().equals(childName) &&
                                    existing.getChildInfo().getDateOfBirth().equals(dob)) {
                                    isDuplicate = true;
                                    break;
                                }
                            }
                        }
                        
                        if (isDuplicate) {
                            Toast.makeText(Activity_birthcertificate.this,
                                "या मुलासाठी आधीच विनंती सबमिट केलेली आहे", Toast.LENGTH_SHORT).show();
                        } else {
                            saveRequestToFirebase();
                        }
                    }

                    @Override
                    public void onCancelled(com.google.firebase.database.DatabaseError error) {
                        Toast.makeText(Activity_birthcertificate.this,
                            "त्रुटी: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Save request to Firebase
     */
    private void saveRequestToFirebase() {
        submitButton.setEnabled(false);
        
        // Create request object
        BirthCertificateRequest request = new BirthCertificateRequest();
        request.setUserId(userMobileNo);
        request.setRequestDate(dateFormat.format(Calendar.getInstance().getTime()));
        request.setStatus("pending");
        request.setIssuingAuthority("रजिस्ट्रार (जन्म व मृत्यू)");
        
        // Child Info
        BirthCertificateRequest.ChildInfo childInfo = new BirthCertificateRequest.ChildInfo(
            childNameMarathi.getText().toString().trim(),
            childNameEnglish.getText().toString().trim(),
            dateOfBirth.getText().toString().trim(),
            placeOfBirth.getText().toString().trim(),
            sexMale.isChecked() ? "पुरुष" : "स्त्री"
        );
        request.setChildInfo(childInfo);
        
        // Mother Info
        String motherAadhaarText = motherAadhaar.getText().toString().trim();
        String motherAadhaarMasked = maskAadhaar(motherAadhaarText);
        BirthCertificateRequest.MotherInfo motherInfo = new BirthCertificateRequest.MotherInfo(
            motherNameMarathi.getText().toString().trim(),
            motherNameEnglish.getText().toString().trim(),
            motherAadhaarMasked
        );
        request.setMotherInfo(motherInfo);
        
        // Father Info
        String fatherAadhaarText = fatherAadhaar.getText().toString().trim();
        String fatherAadhaarMasked = maskAadhaar(fatherAadhaarText);
        BirthCertificateRequest.FatherInfo fatherInfo = new BirthCertificateRequest.FatherInfo(
            fatherNameMarathi.getText().toString().trim(),
            fatherNameEnglish.getText().toString().trim(),
            fatherAadhaarMasked
        );
        request.setFatherInfo(fatherInfo);
        
        // Address Info
        BirthCertificateRequest.AddressInfo addressInfo = new BirthCertificateRequest.AddressInfo(
            birthAddress.getText().toString().trim(),
            permanentAddress.getText().toString().trim()
        );
        request.setAddressInfo(addressInfo);
        
        // Remarks
        request.setRemarks(remarks.getText().toString().trim());
        
        // Save to Firebase
        String requestId = birthCertRef.push().getKey();
        if (requestId != null) {
            request.setRequestId(requestId);
            birthCertRef.child(requestId).setValue(request)
                    .addOnSuccessListener(aVoid -> {
                        submitButton.setEnabled(true);
                        showSuccessDialog(requestId);
                    })
                    .addOnFailureListener(e -> {
                        submitButton.setEnabled(true);
                        Toast.makeText(this, "सबमिशन अयशस्वी: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /**
     * Mask Aadhaar number (XXXX-XXXX-1234)
     */
    private String maskAadhaar(String aadhaar) {
        if (aadhaar.length() == 12) {
            return "XXXX-XXXX-" + aadhaar.substring(8);
        }
        return aadhaar;
    }

    /**
     * Show success dialog with request ID
     */
    private void showSuccessDialog(String requestId) {
        new AlertDialog.Builder(this)
                .setTitle("यशस्वी")
                .setMessage("तुमची विनंती यशस्वीरित्या सबमिट झाली आहे.\n\nविनंती ID: " + requestId)
                .setPositiveButton("ठीक आहे", (dialog, which) -> {
                    dialog.dismiss();
                    finish();
                })
                .setCancelable(false)
                .show();
    }

    /**
     * Load user's certificates
     */
    private void loadMyCertificates() {
        showListLoading();
        
        birthCertRef.orderByChild("userId").equalTo(userMobileNo)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        certificateList.clear();
                        for (DataSnapshot requestSnapshot : snapshot.getChildren()) {
                            BirthCertificateRequest request = requestSnapshot.getValue(BirthCertificateRequest.class);
                            if (request != null) {
                                request.setRequestId(requestSnapshot.getKey());
                                certificateList.add(request);
                            }
                        }
                        
                        // Sort by date (newest first)
                        certificateList.sort((r1, r2) -> 
                            r2.getRequestDate().compareTo(r1.getRequestDate()));
                        
                        hideListLoading();
                        
                        if (certificateList.isEmpty()) {
                            showListEmptyState();
                        } else {
                            hideListEmptyState();
                            setupRecyclerView();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        hideListLoading();
                        Toast.makeText(Activity_birthcertificate.this,
                            "प्रमाणपत्रे लोड करण्यात अडचण: " + error.getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    /**
     * Setup RecyclerView for certificates list
     */
    private void setupRecyclerView() {
        certificateAdapter = new BirthCertificateAdapter(certificateList, 
            new BirthCertificateAdapter.OnCertificateClickListener() {
                @Override
                public void onCertificateClick(BirthCertificateRequest request) {
                    // Show request details with status
                    showRequestDetails(request);
                }
            });
        myCertificatesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myCertificatesRecyclerView.setAdapter(certificateAdapter);
    }
    
    /**
     * Show request details dialog
     */
    private void showRequestDetails(BirthCertificateRequest request) {
        String statusText = "प्रलंबित";
        if ("approved".equalsIgnoreCase(request.getStatus())) {
            statusText = "मंजूर";
        } else if ("rejected".equalsIgnoreCase(request.getStatus())) {
            statusText = "नाकारले";
        }
        
        String details = "विनंती ID: " + request.getRequestId() + "\n\n" +
                        "स्थिती: " + statusText + "\n" +
                        "विनंती तारीख: " + request.getRequestDate();
        
        if (request.getChildInfo() != null) {
            details += "\n\nमुलाचे नाव: " + request.getChildInfo().getNameMarathi() +
                      "\nजन्म तारीख: " + request.getChildInfo().getDateOfBirth();
        }
        
        new AlertDialog.Builder(this)
                .setTitle("विनंती तपशील")
                .setMessage(details)
                .setPositiveButton("ठीक आहे", null)
                .show();
    }
    
    /**
     * Show loading state for list
     */
    private void showListLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        myCertificatesRecyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
    }
    
    /**
     * Hide loading state for list
     */
    private void hideListLoading() {
        loadingLayout.setVisibility(View.GONE);
        myCertificatesRecyclerView.setVisibility(View.VISIBLE);
    }
    
    /**
     * Show empty state for list
     */
    private void showListEmptyState() {
        emptyStateLayout.setVisibility(View.VISIBLE);
        myCertificatesRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
    }
    
    /**
     * Hide empty state for list
     */
    private void hideListEmptyState() {
        emptyStateLayout.setVisibility(View.GONE);
        myCertificatesRecyclerView.setVisibility(View.VISIBLE);
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

