package com.example.mazigrampanchayat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Activity_news extends AppCompatActivity {

    private RecyclerView newsRecyclerView;
    private LinearLayout loadingLayout;
    private ProgressBar progressBar;
    private TextView loadingMessage;
    private NewsAdapter newsAdapter;
    private ArrayList<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
            EdgeToEdge.enable(this);
        // Initialize views
        loadingLayout = findViewById(R.id.loadingLayout);
        progressBar = findViewById(R.id.progressBar);
        loadingMessage = findViewById(R.id.loadingMessage);
        newsRecyclerView = findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        newsRecyclerView.setAdapter(newsAdapter);

        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v-> finish());
        fetchNews();
    }

    private void fetchNews() {
        loadingLayout.setVisibility(View.VISIBLE);
        newsRecyclerView.setVisibility(View.GONE);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("news");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    News news = dataSnapshot.getValue(News.class);
                    newsList.add(news);
                }
                newsAdapter.notifyDataSetChanged();

                loadingLayout.setVisibility(View.GONE);
                newsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                loadingLayout.setVisibility(View.GONE);
                newsRecyclerView.setVisibility(View.VISIBLE);
                Toast.makeText(Activity_news.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
