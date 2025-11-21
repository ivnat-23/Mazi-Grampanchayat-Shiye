package com.example.mazigrampanchayat;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class Activity_welcome extends AppCompatActivity {

    private RelativeLayout noInternetLayout;
    private RelativeLayout mainContentLayout;
    private ImageView noInternetImageView;
    private Button retryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        EdgeToEdge.enable(this);
        noInternetLayout = findViewById(R.id.noInternet);
        mainContentLayout = findViewById(R.id.main);
        retryButton = findViewById(R.id.button);

        checkInternetConnection();

        retryButton.setOnClickListener(v -> {
            if (isInternetAvailable()) {
                showMainContent();
            } else {
                Toast.makeText(this, "No internet connection. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Check the internet connection and update the UI accordingly.
     */
    private void checkInternetConnection() {
        if (isInternetAvailable()) {
            showMainContent();
        } else {
            showNoInternetScreen();
        }
    }

    /**
     * Display the main content layout and hide the no internet layout.
     */
    private void showMainContent() {
        noInternetLayout.setVisibility(View.GONE);
        mainContentLayout.setVisibility(View.VISIBLE);
        Button login = findViewById(R.id.login);
        TextView registration = findViewById(R.id.registration);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_welcome.this, Activity_login.class);
                startActivity(intent);
            }
        });

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Activity_welcome.this, Activity_registration.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Display the no internet layout and hide the main content layout.
     */
    private void showNoInternetScreen() {
        mainContentLayout.setVisibility(View.GONE);
        noInternetLayout.setVisibility(View.VISIBLE);
    }

    /**
     * Check if the device is connected to the internet.
     *
     * @return true if connected, false otherwise
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
