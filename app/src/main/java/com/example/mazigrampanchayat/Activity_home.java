package com.example.mazigrampanchayat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class Activity_home extends AppCompatActivity {
    private ImageCarousel imageCarousel;
    ImageView idContact;
    ImageView idJalJivanMission;
    ImageView idfinalcial;
    ImageView idfreedom;
    ImageView idnews;
    ImageView idvillage;
    ImageView idwater;
    ImageView idNoticeBoard;
    ImageView idSuggestionBox;
    ImageView idBirthCertificate;
    TextView setting;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        imageCarousel = findViewById(R.id.Carousle);
        setUpCarousel();

        this.idvillage = (ImageView)this.findViewById(R.id.idvillage);
        this.idfreedom = (ImageView)this.findViewById(R.id.idfreedom);
        this.idnews = (ImageView)this.findViewById(R.id.idnews);
        this.idwater = (ImageView)this.findViewById(R.id.idwater);
        this.idContact = (ImageView)this.findViewById(R.id.idContact);
        this.idfinalcial = (ImageView)this.findViewById(R.id.idfinalcial);
        this.idJalJivanMission = (ImageView)this.findViewById(R.id.idJalJivanMission);
        this.idNoticeBoard = (ImageView)this.findViewById(R.id.idNoticeBoard);
        this.idSuggestionBox = (ImageView)this.findViewById(R.id.idSuggestionBox);
        this.idBirthCertificate = (ImageView)this.findViewById(R.id.idBirthCertificate);


        this.idContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_contact.class));
            }
        });
        this.idvillage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_about.class));
            }
        });
        this.idwater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_watersupply.class));
            }
        });
        this.idnews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_news.class));
            }
        });

        this.idfreedom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_members.class));
            }
        });

        this.idfinalcial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_finanace.class));
            }
        });

        this.idJalJivanMission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, jaljivaanmission.class));
            }
        });

        this.idNoticeBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_noticeboard.class));
            }
        });

        this.idSuggestionBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_suggestionbox.class));
            }
        });

        this.idBirthCertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Activity_home.this, Activity_birthcertificate.class));
            }
        });

        //Profile Button
        ImageButton profile = findViewById(R.id.ProfileButton);
        profile.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_home.this, Activity_profile.class);
            startActivity(intent);
        });
    }
    private void setUpCarousel() {
        List<CarouselItem> items = new ArrayList<>();

        items.add(new CarouselItem(R.drawable.ic_images1));
        items.add(new CarouselItem(R.drawable.ic_carousal2));


        imageCarousel.addData(items);

        imageCarousel.setAutoPlay(true);
    }
    private void logoutUser() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(Activity_home.this, Activity_welcome.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}