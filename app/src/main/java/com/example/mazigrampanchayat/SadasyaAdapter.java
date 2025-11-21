package com.example.mazigrampanchayat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SadasyaAdapter extends RecyclerView.Adapter<SadasyaAdapter.ViewHolder> {

    private List<Sadasya> sadasyaList;

    public SadasyaAdapter(List<Sadasya> sadasyaList) {
        this.sadasyaList = sadasyaList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sadasya, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Sadasya sadasya = sadasyaList.get(position);
        holder.nameTextView.setText(sadasya.getName());
        holder.numberTextView.setText(sadasya.getNumber());
        Picasso.get().load(sadasya.getPhotoUrl()).into(holder.photoImageView);
    }

    @Override
    public int getItemCount() {
        return sadasyaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, numberTextView;
        ImageView photoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sadasyaName);
            numberTextView = itemView.findViewById(R.id.sadasyaNumber);
            photoImageView = itemView.findViewById(R.id.sadasyaPhoto);
        }
    }
}
