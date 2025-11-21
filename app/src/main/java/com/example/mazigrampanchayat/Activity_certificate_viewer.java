package com.example.mazigrampanchayat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Activity for viewing and downloading birth certificate PDF
 * Handles Base64 PDF decoding, viewing, and saving to device
 */
public class Activity_certificate_viewer extends AppCompatActivity {

    private ImageButton backButton, downloadButton, shareButton;
    private TextView certificateTitle;
    private ProgressBar progressBar;
    private String pdfBase64;
    private String requestId;
    private String childName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_certificate_viewer);

        // Get data from intent
        pdfBase64 = getIntent().getStringExtra("certificate_pdf_base64");
        requestId = getIntent().getStringExtra("request_id");
        childName = getIntent().getStringExtra("child_name");

        initializeViews();
        setupButtons();
        
        if (pdfBase64 != null && !pdfBase64.isEmpty()) {
            // PDF is already generated and stored as Base64
            // For now, show download option
            // In future, can implement PDF rendering using PdfRenderer
            certificateTitle.setText("प्रमाणपत्र तयार आहे");
        } else {
            certificateTitle.setText("प्रमाणपत्र अजून तयार झालेले नाही");
            downloadButton.setEnabled(false);
        }
    }

    /**
     * Initialize views
     */
    private void initializeViews() {
        backButton = findViewById(R.id.backButton);
        downloadButton = findViewById(R.id.downloadButton);
        shareButton = findViewById(R.id.shareButton);
        certificateTitle = findViewById(R.id.certificateTitle);
        progressBar = findViewById(R.id.progressBar);
    }

    /**
     * Setup button listeners
     */
    private void setupButtons() {
        backButton.setOnClickListener(v -> finish());
        
        downloadButton.setOnClickListener(v -> {
            if (pdfBase64 != null && !pdfBase64.isEmpty()) {
                downloadCertificate();
            } else {
                Toast.makeText(this, "प्रमाणपत्र अजून उपलब्ध नाही", Toast.LENGTH_SHORT).show();
            }
        });
        
        shareButton.setOnClickListener(v -> {
            if (pdfBase64 != null && !pdfBase64.isEmpty()) {
                shareCertificate();
            } else {
                Toast.makeText(this, "प्रमाणपत्र अजून उपलब्ध नाही", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Download certificate PDF to device
     */
    private void downloadCertificate() {
        try {
            // Decode Base64 to byte array
            String base64Data = pdfBase64;
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }
            byte[] pdfBytes = Base64.decode(base64Data, Base64.DEFAULT);
            
            // Save to Downloads folder
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            String fileName = "BirthCertificate_" + requestId + ".pdf";
            File pdfFile = new File(downloadsDir, fileName);
            
            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(pdfBytes);
            fos.close();
            
            Toast.makeText(this, "प्रमाणपत्र डाउनलोड झाले: " + fileName, Toast.LENGTH_LONG).show();
            
            // Open file location
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(pdfFile);
            intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "डाउनलोड अयशस्वी: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Share certificate PDF
     */
    private void shareCertificate() {
        try {
            // Decode Base64 to byte array
            String base64Data = pdfBase64;
            if (base64Data.contains(",")) {
                base64Data = base64Data.split(",")[1];
            }
            byte[] pdfBytes = Base64.decode(base64Data, Base64.DEFAULT);
            
            // Save to cache temporarily
            File cacheDir = getCacheDir();
            String fileName = "BirthCertificate_" + requestId + ".pdf";
            File pdfFile = new File(cacheDir, fileName);
            
            FileOutputStream fos = new FileOutputStream(pdfFile);
            fos.write(pdfBytes);
            fos.close();
            
            // Share file
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/pdf");
            Uri uri = Uri.fromFile(pdfFile);
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "जन्म प्रमाणपत्र - " + childName);
            startActivity(Intent.createChooser(shareIntent, "प्रमाणपत्र शेअर करा"));
            
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "शेअर अयशस्वी: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}

