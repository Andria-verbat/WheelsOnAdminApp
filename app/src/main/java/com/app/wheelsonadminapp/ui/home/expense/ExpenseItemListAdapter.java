package com.app.wheelsonadminapp.ui.home.expense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.expense.ExpenseItem;

import java.util.List;

public class ExpenseItemListAdapter extends RecyclerView.Adapter<ExpenseItemListAdapter.ExpenseItemViewHolder>{

    private List<ExpenseItem>expenseItems;
    ExpenseItemClickListener expenseItemClickListener;

    public ExpenseItemListAdapter(List<ExpenseItem> expenseItems, ExpenseItemClickListener expenseItemClickListener) {
        this.expenseItems = expenseItems;
        this.expenseItemClickListener = expenseItemClickListener;
    }

    @NonNull
    @Override
    public ExpenseItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row, parent, false);
        ExpenseItemViewHolder vh = new ExpenseItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseItemViewHolder holder, int position) {
        holder.mTextView.setText(expenseItems.get(position).getExpensetypename());
    }

    @Override
    public int getItemCount() {
        if(expenseItems!=null){
            return expenseItems.size();
        }else {
            return 0;
        }
    }

    public class ExpenseItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public ExpenseItemViewHolder(@NonNull View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            expenseItemClickListener.clickOnItem(expenseItems.get(this.getAdapterPosition()));
        }
    }

    public interface ExpenseItemClickListener {
        void clickOnItem(ExpenseItem data);
    }
}
