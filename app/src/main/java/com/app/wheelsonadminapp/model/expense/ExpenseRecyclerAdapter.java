package com.app.wheelsonadminapp.model.expense;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.service.VehicleServiceItem;
import com.app.wheelsonadminapp.ui.home.service.ServiceRecyclerAdapter;

import java.util.List;

public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseRecyclerAdapter.ExpenseViewHolder> {

    List<VehicleExpenseItem> vehicleExpenseItems;
    Context context;
    ExpenseItemClickListener expenseItemClickListener;

    public ExpenseRecyclerAdapter(List<VehicleExpenseItem> vehicleExpenseItems, Context context, ExpenseItemClickListener expenseItemClickListener) {
        this.vehicleExpenseItems = vehicleExpenseItems;
        this.context = context;
        this.expenseItemClickListener = expenseItemClickListener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_row, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        VehicleExpenseItem vehicleExpenseItem =vehicleExpenseItems.get(position);
        holder.textServiceName.setText(vehicleExpenseItem.getExpenseName());
        holder.textServiceValue.setText("Rs."+vehicleExpenseItem.getExpenseValue());
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseItemClickListener.onDeleteClicked(position,vehicleExpenseItem);
            }
        });
        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expenseItemClickListener.onItemSelected(position,vehicleExpenseItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vehicleExpenseItems.size();
    }

    public class ExpenseViewHolder extends RecyclerView.ViewHolder{

        TextView textServiceName,textServiceValue;
        ImageView imgDelete;
        CardView parentCardView;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            textServiceName = itemView.findViewById(R.id.textName);
            textServiceValue = itemView.findViewById(R.id.textValue);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            parentCardView = itemView.findViewById(R.id.parentCardView);
        }
    }

    public interface ExpenseItemClickListener{
        void onDeleteClicked(int position, VehicleExpenseItem vehicleExpenseItem);
        void onItemSelected(int position,VehicleExpenseItem vehicleExpenseItem);
    }
}
