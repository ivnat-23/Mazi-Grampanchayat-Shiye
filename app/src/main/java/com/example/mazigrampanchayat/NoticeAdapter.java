package com.example.mazigrampanchayat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Adapter for displaying notices in RecyclerView
 * Handles Base64 image decoding and priority-based sorting
 */
public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder> {

    private ArrayList<Notice> noticeList;
    private OnNoticeClickListener listener;

    public interface OnNoticeClickListener {
        void onNoticeClick(Notice notice);
    }

    public NoticeAdapter(ArrayList<Notice> noticeList, OnNoticeClickListener listener) {
        this.noticeList = noticeList;
        this.listener = listener;
        sortNotices();
    }

    /**
     * Sort notices by date (newest first) and then by priority (high > medium > low)
     */
    private void sortNotices() {
        Collections.sort(noticeList, new Comparator<Notice>() {
            @Override
            public int compare(Notice n1, Notice n2) {
                // First sort by date (newest first)
                int dateCompare = n2.getDate().compareTo(n1.getDate());
                if (dateCompare != 0) {
                    return dateCompare;
                }
                // Then sort by priority
                int priority1 = getPriorityValue(n1.getPriority());
                int priority2 = getPriorityValue(n2.getPriority());
                return Integer.compare(priority2, priority1); // Higher priority first
            }

            private int getPriorityValue(String priority) {
                if (priority == null) return 0;
                switch (priority.toLowerCase()) {
                    case "high":
                        return 3;
                    case "medium":
                        return 2;
                    case "low":
                        return 1;
                    default:
                        return 0;
                }
            }
        });
    }

    @NonNull
    @Override
    public NoticeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notice, parent, false);
        return new NoticeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoticeViewHolder holder, int position) {
        Notice notice = noticeList.get(position);
        
        holder.noticeTitle.setText(notice.getTitle());
        holder.noticeDate.setText(notice.getDate());
        
        // Set priority badge color
        int priorityColor = getPriorityColor(notice.getPriority(), holder.itemView.getContext());
        holder.priorityBadge.setBackgroundColor(priorityColor);
        
        // Decode and display Base64 image
        if (notice.getImageBase64() != null && !notice.getImageBase64().isEmpty()) {
            Bitmap bitmap = decodeBase64Image(notice.getImageBase64());
            if (bitmap != null) {
                holder.noticeImage.setImageBitmap(bitmap);
            }
        }
        
        // Set click listener
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onNoticeClick(notice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return noticeList.size();
    }

    /**
     * Decode Base64 string to Bitmap
     */
    private Bitmap decodeBase64Image(String base64String) {
        try {
            // Remove data URL prefix if present
            if (base64String.contains(",")) {
                base64String = base64String.split(",")[1];
            }
            byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get color for priority badge
     */
    private int getPriorityColor(String priority, android.content.Context context) {
        if (priority == null) {
            return ContextCompat.getColor(context, R.color.priority_low);
        }
        switch (priority.toLowerCase()) {
            case "high":
                return ContextCompat.getColor(context, R.color.priority_high);
            case "medium":
                return ContextCompat.getColor(context, R.color.priority_medium);
            case "low":
                return ContextCompat.getColor(context, R.color.priority_low);
            default:
                return ContextCompat.getColor(context, R.color.priority_low);
        }
    }

    static class NoticeViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView noticeImage;
        TextView noticeTitle;
        TextView noticeDate;
        View priorityBadge;

        public NoticeViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.noticeCardView);
            noticeImage = itemView.findViewById(R.id.noticeImage);
            noticeTitle = itemView.findViewById(R.id.noticeTitle);
            noticeDate = itemView.findViewById(R.id.noticeDate);
            priorityBadge = itemView.findViewById(R.id.priorityBadge);
        }
    }
}

