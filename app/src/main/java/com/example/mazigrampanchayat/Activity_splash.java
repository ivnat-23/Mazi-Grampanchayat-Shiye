package com.example.mazigrampanchayat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Activity_splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView imageView = findViewById(R.id.imageView);
        TextView textView = findViewById(R.id.textView);

        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(Activity_splash.this, Activity_home.class);
                startActivity(intent);
                finish();
            }, 1300);
        } else {
            Animation combinedAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in_scale_slide_up);
            imageView.startAnimation(combinedAnimation);
            textView.startAnimation(combinedAnimation);

            combinedAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) { }

                @Override
                public void onAnimationEnd(Animation animation) {
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(Activity_splash.this, Activity_welcome.class);
                        startActivity(intent);
                        finish();
                    }, 1300);
                }

                @Override
                public void onAnimationRepeat(Animation animation) { }
            });
        }
    }
}
