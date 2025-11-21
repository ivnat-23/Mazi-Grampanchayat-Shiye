package com.example.mazigrampanchayat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FinanceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int VIEW_TYPE_INCOME = 0;
    private static final int VIEW_TYPE_EXPENSE = 1;

    private List<Object> financeList;

    public FinanceAdapter(List<Object> financeList) {
        this.financeList = financeList;
    }

    @Override
    public int getItemViewType(int position) {
        if (financeList.get(position) instanceof Income) {
            return VIEW_TYPE_INCOME;
        } else {
            return VIEW_TYPE_EXPENSE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_INCOME) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_item, parent, false);
            return new IncomeViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_item, parent, false);
            return new ExpenseViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_INCOME) {
            Income income = (Income) financeList.get(position);
            ((IncomeViewHolder) holder).bind(income);
        } else {
            Expense expense = (Expense) financeList.get(position);
            ((ExpenseViewHolder) holder).bind(expense);
        }
    }

    @Override
    public int getItemCount() {
        return financeList.size();
    }

    static class IncomeViewHolder extends RecyclerView.ViewHolder {
        TextView sourceTextView, amountTextView, dateTextView;

        IncomeViewHolder(@NonNull View itemView) {
            super(itemView);
            sourceTextView = itemView.findViewById(R.id.sourceTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        void bind(Income income) {
            sourceTextView.setText("स्रोत: " + income.getस्रोत());
            amountTextView.setText("रक्कम: " + income.getरक्कम());
            dateTextView.setText("दिनांक: " + income.getदिनांक());
        }
    }

    static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView purposeTextView, amountTextView, dateTextView;

        ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            purposeTextView = itemView.findViewById(R.id.purposeTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
        }

        void bind(Expense expense) {
            purposeTextView.setText("उद्दिष्ट: " + expense.getउद्दिष्ट());
            amountTextView.setText("रक्कम: " + expense.getरक्कम());
            dateTextView.setText("दिनांक: " + expense.getदिनांक());
        }
    }
}
