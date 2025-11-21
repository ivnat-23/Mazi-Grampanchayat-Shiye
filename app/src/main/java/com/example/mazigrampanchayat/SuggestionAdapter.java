package com.example.mazigrampanchayat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter for displaying user suggestions in RecyclerView
 * Shows suggestion details with status badges
 */
public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    private ArrayList<Suggestion> suggestionList;

    public SuggestionAdapter(ArrayList<Suggestion> suggestionList) {
        this.suggestionList = suggestionList;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_suggestion, parent, false);
        return new SuggestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionViewHolder holder, int position) {
        Suggestion suggestion = suggestionList.get(position);
        
        holder.categoryText.setText(suggestion.getCategory());
        holder.suggestionText.setText(suggestion.getSuggestion());
        holder.dateText.setText(suggestion.getSubmissionDate());
        holder.timeText.setText(suggestion.getSubmissionTime());
        
        // Set status badge
        String status = suggestion.getStatus();
        if (status != null) {
            switch (status.toLowerCase()) {
                case "pending":
                    holder.statusBadge.setText("प्रलंबित");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending));
                    break;
                case "reviewed":
                    holder.statusBadge.setText("पुनरावलोकन");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.status_reviewed));
                    break;
                case "implemented":
                    holder.statusBadge.setText("अंमलात आणले");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.status_implemented));
                    break;
                default:
                    holder.statusBadge.setText("प्रलंबित");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending));
            }
        }
        
        // Show admin response if available
        if (suggestion.getAdminResponse() != null && !suggestion.getAdminResponse().isEmpty()) {
            holder.adminResponseLayout.setVisibility(View.VISIBLE);
            holder.adminResponseText.setText(suggestion.getAdminResponse());
        } else {
            holder.adminResponseLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return suggestionList.size();
    }

    static class SuggestionViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView categoryText;
        TextView suggestionText;
        TextView dateText;
        TextView timeText;
        TextView statusBadge;
        ViewGroup adminResponseLayout;
        TextView adminResponseText;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.suggestionCardView);
            categoryText = itemView.findViewById(R.id.categoryText);
            suggestionText = itemView.findViewById(R.id.suggestionText);
            dateText = itemView.findViewById(R.id.dateText);
            timeText = itemView.findViewById(R.id.timeText);
            statusBadge = itemView.findViewById(R.id.statusBadge);
            adminResponseLayout = itemView.findViewById(R.id.adminResponseLayout);
            adminResponseText = itemView.findViewById(R.id.adminResponseText);
        }
    }
}

