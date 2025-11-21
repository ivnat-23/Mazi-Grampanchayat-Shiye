package com.example.mazigrampanchayat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Activity for displaying Gram Panchayat notices
 * Features: Pull-to-refresh, sorting by date and priority, full-screen image view
 */
public class Activity_noticeboard extends AppCompatActivity {

    private RecyclerView noticesRecyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout loadingLayout;
    private LinearLayout emptyStateLayout;
    private ProgressBar progressBar;
    private TextView loadingMessage;
    private NoticeAdapter noticeAdapter;
    private ArrayList<Notice> noticeList;
    private DatabaseReference noticesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_noticeboard);

        initializeViews();
        setupRecyclerView();
        setupSwipeRefresh();
        setupBackButton();
        
        // Check internet connectivity
        if (isInternetAvailable()) {
            fetchNotices();
        } else {
            showEmptyState();
            Toast.makeText(this, "इंटरनेट कनेक्शन उपलब्ध नाही", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Initialize all views
     */
    private void initializeViews() {
        noticesRecyclerView = findViewById(R.id.noticesRecyclerView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        loadingLayout = findViewById(R.id.loadingLayout);
        emptyStateLayout = findViewById(R.id.emptyStateLayout);
        progressBar = findViewById(R.id.progressBar);
        loadingMessage = findViewById(R.id.loadingMessage);
        
        noticeList = new ArrayList<>();
        noticesRef = FirebaseDatabase.getInstance().getReference("notices");
    }

    /**
     * Setup RecyclerView with adapter
     */
    private void setupRecyclerView() {
        noticeAdapter = new NoticeAdapter(noticeList, new NoticeAdapter.OnNoticeClickListener() {
            @Override
            public void onNoticeClick(Notice notice) {
                openNoticeDetail(notice);
            }
        });
        noticesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        noticesRecyclerView.setAdapter(noticeAdapter);
    }

    /**
     * Setup pull-to-refresh functionality
     */
    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.primary_blue),
                getResources().getColor(R.color.accent_green)
        );
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (isInternetAvailable()) {
                fetchNotices();
            } else {
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(this, "इंटरनेट कनेक्शन उपलब्ध नाही", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Setup back button
     */
    private void setupBackButton() {
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
    }

    /**
     * Fetch notices from Firebase
     */
    private void fetchNotices() {
        showLoading();
        
        noticesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                noticeList.clear();
                
                if (!snapshot.exists()) {
                    hideLoading();
                    showEmptyState();
                    if (swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                    return;
                }
                
                for (DataSnapshot noticeSnapshot : snapshot.getChildren()) {
                    try {
                        Notice notice = noticeSnapshot.getValue(Notice.class);
                        if (notice != null) {
                            // Check if notice is active (default to true if not set)
                            boolean isActive = notice.isActive();
                            if (isActive) {
                                notice.setNoticeId(noticeSnapshot.getKey());
                                noticeList.add(notice);
                            }
                        }
                    } catch (Exception e) {
                        // Log error but continue processing other notices
                        e.printStackTrace();
                    }
                }
                
                noticeAdapter.notifyDataSetChanged();
                hideLoading();
                
                if (noticeList.isEmpty()) {
                    showEmptyState();
                } else {
                    hideEmptyState();
                }
                
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                hideLoading();
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Toast.makeText(Activity_noticeboard.this, 
                    "सूचना लोड करण्यात अडचण आली: " + error.getMessage(), 
                    Toast.LENGTH_SHORT).show();
                showEmptyState();
            }
        });
    }

    /**
     * Open notice detail in full-screen with pinch-to-zoom
     */
    private void openNoticeDetail(Notice notice) {
        Intent intent = new Intent(this, Activity_notice_detail.class);
        intent.putExtra("notice_title", notice.getTitle());
        intent.putExtra("notice_image", notice.getImageBase64());
        startActivity(intent);
    }

    /**
     * Show loading state
     */
    private void showLoading() {
        loadingLayout.setVisibility(View.VISIBLE);
        noticesRecyclerView.setVisibility(View.GONE);
        emptyStateLayout.setVisibility(View.GONE);
    }

    /**
     * Hide loading state
     */
    private void hideLoading() {
        loadingLayout.setVisibility(View.GONE);
        noticesRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * Show empty state
     */
    private void showEmptyState() {
        emptyStateLayout.setVisibility(View.VISIBLE);
        noticesRecyclerView.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
    }

    /**
     * Hide empty state
     */
    private void hideEmptyState() {
        emptyStateLayout.setVisibility(View.GONE);
        noticesRecyclerView.setVisibility(View.VISIBLE);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Note: Using addListenerForSingleValueEvent automatically removes listener after execution
        // No need to manually remove for single value events
    }
}

