package com.example.mazigrampanchayat;

import android.os.Bundle;
import android.widget.ImageButton;
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
import java.util.List;

public class Activity_finanace extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FinanceAdapter financeAdapter;
    private List<Object> financeList;
    private DatabaseReference financeRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finanace);
        EdgeToEdge.enable(this);

        recyclerView = findViewById(R.id.financialRVL);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        financeList = new ArrayList<>();
        financeAdapter = new FinanceAdapter(financeList);
        recyclerView.setAdapter(financeAdapter);
        ImageButton back = findViewById(R.id.backButton);
        back.setOnClickListener(v -> finish());
        financeRef = FirebaseDatabase.getInstance().getReference("grampanchayat_finance/शिये/2023-24");

        financeRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                financeList.clear();

                DataSnapshot incomeSnapshot = snapshot.child("उत्पन्न");
                for (DataSnapshot data : incomeSnapshot.getChildren()) {
                    Income income = data.getValue(Income.class);
                    financeList.add(income);
                }

                DataSnapshot expenseSnapshot = snapshot.child("खर्च");
                for (DataSnapshot data : expenseSnapshot.getChildren()) {
                    Expense expense = data.getValue(Expense.class);
                    financeList.add(expense);
                }

                financeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Activity_finanace.this, "डेटा लोड करण्यात अडचण आली.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

