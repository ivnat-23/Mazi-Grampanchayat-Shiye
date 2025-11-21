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
 * Adapter for displaying birth certificate requests in RecyclerView
 * Shows request details with status badges
 */
public class BirthCertificateAdapter extends RecyclerView.Adapter<BirthCertificateAdapter.CertificateViewHolder> {

    private ArrayList<BirthCertificateRequest> requestList;
    private OnCertificateClickListener listener;

    public interface OnCertificateClickListener {
        void onCertificateClick(BirthCertificateRequest request);
    }

    public BirthCertificateAdapter(ArrayList<BirthCertificateRequest> requestList, OnCertificateClickListener listener) {
        this.requestList = requestList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CertificateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_birth_certificate, parent, false);
        return new CertificateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateViewHolder holder, int position) {
        BirthCertificateRequest request = requestList.get(position);
        
        if (request.getChildInfo() != null) {
            holder.childNameText.setText(request.getChildInfo().getNameMarathi());
            holder.dateOfBirthText.setText(request.getChildInfo().getDateOfBirth());
        }
        
        holder.requestDateText.setText(request.getRequestDate());
        holder.requestIdText.setText("ID: " + request.getRequestId());
        
        // Set status badge
        String status = request.getStatus();
        if (status != null) {
            switch (status.toLowerCase()) {
                case "pending":
                    holder.statusBadge.setText("प्रलंबित");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending));
                    break;
                case "approved":
                    holder.statusBadge.setText("मंजूर");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.status_implemented));
                    break;
                case "rejected":
                    holder.statusBadge.setText("नाकारले");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.error_red));
                    break;
                default:
                    holder.statusBadge.setText("प्रलंबित");
                    holder.statusBadge.setBackgroundColor(
                        ContextCompat.getColor(holder.itemView.getContext(), R.color.status_pending));
            }
        }
        
        // Set click listener - only show status details
        holder.cardView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCertificateClick(request);
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    static class CertificateViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView childNameText;
        TextView dateOfBirthText;
        TextView requestDateText;
        TextView requestIdText;
        TextView statusBadge;

        public CertificateViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.certificateCardView);
            childNameText = itemView.findViewById(R.id.childNameText);
            dateOfBirthText = itemView.findViewById(R.id.dateOfBirthText);
            requestDateText = itemView.findViewById(R.id.requestDateText);
            requestIdText = itemView.findViewById(R.id.requestIdText);
            statusBadge = itemView.findViewById(R.id.statusBadge);
        }
    }
}

