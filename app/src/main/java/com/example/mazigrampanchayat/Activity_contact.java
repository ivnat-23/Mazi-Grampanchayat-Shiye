package com.example.mazigrampanchayat;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_contact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_contact);
        //back button
        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());


        ImageButton callSarpanchButton = findViewById(R.id.callSarpanchButton);
        callSarpanchButton.setOnClickListener(v -> openDialer("9766671024"));


        ImageButton callUpSarpanchButton = findViewById(R.id.callUpSarpanchButton);
        callUpSarpanchButton.setOnClickListener(v -> openDialer("8625076026"));


        ImageButton callAdhikariButton = findViewById(R.id.calladhikariButton);
        callAdhikariButton.setOnClickListener(v -> openDialer("7887979190"));


        ImageButton callTalathiButton = findViewById(R.id.callTlathiButton);
        callTalathiButton.setOnClickListener(v -> openDialer("9975467674"));


        ImageButton callHospitalButton = findViewById(R.id.callHospitalButton);
        callHospitalButton.setOnClickListener(v -> openDialer("7387296819"));



    }

    private void openDialer(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }
}
