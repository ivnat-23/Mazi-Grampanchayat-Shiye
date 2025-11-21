package com.example.mazigrampanchayat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class Activity_members extends AppCompatActivity {

    private TextView sarpanchName, sarpanchNumber,sarpanchWrad, upsarpanchName, upsarpanchNumber,upsarpanchWrad;
    private ImageView sarpanchImage, upsarpanchImage;
    private RecyclerView recyclerView;
    private MemberAdapter adapter;
    private ArrayList<Member> memberList;
    private LinearLayout main_layout;
    private LinearLayout loadingLayout;
    private ProgressBar progressBar;
    private TextView loadingMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_members);
        EdgeToEdge.enable(this);
        sarpanchName = findViewById(R.id.sarpanchName);
        sarpanchNumber = findViewById(R.id.sarpanchNumber);
        sarpanchWrad = findViewById(R.id.sarpanchWard);
        sarpanchImage = findViewById(R.id.sarpanchImage);

        upsarpanchName = findViewById(R.id.upsarpanchName);
        upsarpanchNumber = findViewById(R.id.upsarpanchNumber);
        upsarpanchWrad = findViewById(R.id.upsarpanchWard);

        upsarpanchImage = findViewById(R.id.upsarpanchImage);

        recyclerView = findViewById(R.id.sadasyaRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadingLayout = findViewById(R.id.loadingLayout);
        progressBar = findViewById(R.id.progressBar);
        loadingMessage = findViewById(R.id.loadingMessage);
        main_layout = findViewById(R.id.main_content);

        memberList = new ArrayList<>();
        adapter = new MemberAdapter(memberList);
        recyclerView.setAdapter(adapter);
        loadingLayout.setVisibility(View.VISIBLE);
        main_layout.setVisibility(View.GONE);
        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v-> finish());
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("grampanchayat_members");

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DataSnapshot sarpanchSnapshot = dataSnapshot.child("sarpanch");
                String sarpanchNameText = sarpanchSnapshot.child("name").getValue(String.class);
                String sarpanchNumberText = sarpanchSnapshot.child("number").getValue(String.class);
                String sarpanchPhotoUrl = sarpanchSnapshot.child("photoUrl").getValue(String.class);
                String sarpanchWardText = sarpanchSnapshot.child("ward").getValue(String.class);
                sarpanchName.setText(sarpanchNameText);
                sarpanchNumber.setText(sarpanchNumberText);
                sarpanchWrad.setText((sarpanchWardText));
                Glide.with(Activity_members.this).load(sarpanchPhotoUrl).into(sarpanchImage);

                DataSnapshot upsarpanchSnapshot = dataSnapshot.child("upsarpanch");
                String upsarpanchNameText = upsarpanchSnapshot.child("name").getValue(String.class);
                String upsarpanchNumberText = upsarpanchSnapshot.child("number").getValue(String.class);
                String upsarpanchPhotoUrl = upsarpanchSnapshot.child("photoUrl").getValue(String.class);
                String upsarpanchWardText = upsarpanchSnapshot.child("ward").getValue(String.class);

               loadingLayout.setVisibility(View.GONE);
                main_layout.setVisibility(View.VISIBLE);
                upsarpanchName.setText(upsarpanchNameText);
                upsarpanchNumber.setText(upsarpanchNumberText);
                upsarpanchWrad.setText((upsarpanchWardText));
                Glide.with(Activity_members.this).load(upsarpanchPhotoUrl).into(upsarpanchImage);

                DataSnapshot sadasyaSnapshot = dataSnapshot.child("sadasya");
                for (DataSnapshot sadasya : sadasyaSnapshot.getChildren()) {
                    String sadasyaName = sadasya.child("name").getValue(String.class);
                    String sadasyaNumber = sadasya.child("number").getValue(String.class);
                    String sadasyaPhotoUrl = sadasya.child("photoUrl").getValue(String.class);
                    String sadasyaWard = sadasya.child("ward").getValue(String.class);
                    memberList.add(new Member(sadasyaName, sadasyaNumber, sadasyaPhotoUrl,sadasyaWard));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

class Member {
    String name, number, photoUrl,ward;

    public Member(String name, String number, String photoUrl, String ward) {
        this.name = name;
        this.number = number;
        this.photoUrl = photoUrl;
        this.ward = ward;
    }
}

class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private ArrayList<Member> members;

    public MemberAdapter(ArrayList<Member> members) {
        this.members = members;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Member member = members.get(position);
        holder.nameTextView.setText(member.name);
        holder.numberTextView.setText(member.number);
        Glide.with(holder.itemView.getContext())
                .load(member.photoUrl)
                .into(holder.photoImageView);
        holder.wardTextView.setText(member.ward);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, numberTextView,wardTextView;
        ImageView photoImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.sadasyaName);
            numberTextView = itemView.findViewById(R.id.sadasyaNumber);
            photoImageView = itemView.findViewById(R.id.sadasyaPhoto);
            wardTextView = itemView.findViewById(R.id.sadasyaWardNumber);
        }
    }
}
