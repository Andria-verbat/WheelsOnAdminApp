package com.app.wheelsonadminapp.ui.home.closed_trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.driver.DriverItem;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseItem;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseResponse;
import com.app.wheelsonadminapp.model.expense.closedTrip.ClosedTripExpenseResponseItem;
import com.app.wheelsonadminapp.model.trip.closed_trips.ClosedTripItems;
import com.app.wheelsonadminapp.util.AppConstants;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Andria on 2/9/2022.
 */
public class ClosedTripExpenseAdapter extends RecyclerView.Adapter<ClosedTripExpenseAdapter.ClosedTripExpenseViewModel> {

    List<ClosedTripExpenseItem> closedTripItems;

    Context context;
    String path;
    ExpenseClickListener expenseClickListener;


    public ClosedTripExpenseAdapter(List<ClosedTripExpenseItem> closedTripItems, Context context, String path, ExpenseClickListener expenseClickListener) {
        this.closedTripItems = closedTripItems;
        this.context = context;
        this.path = path;
        this.expenseClickListener = expenseClickListener;


    }

    @NonNull
    @Override
    public ClosedTripExpenseViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.closed_expense_recycler, parent, false);
        return new ClosedTripExpenseViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ClosedTripExpenseViewModel holder, int position) {
        ClosedTripExpenseItem closedTripItem = closedTripItems.get(position);
        holder.textExpenseName.setText("Expense Name : " + closedTripItem.getExpensetype());
        holder.textAmount.setText("Amount : " + closedTripItem.getExpense_amount());
        String imgUrl = AppConstants.SERVER_URL + path + closedTripItem.getExpenseimage();
        Picasso.get().load(imgUrl)
                .centerCrop().placeholder(R.drawable.ic_baseline_payments_24).resize(125, 125).into(holder.imgProfile);


        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseClickListener.onExpenseClicked(closedTripItem, closedTripItem.getExpenseimage());

            }
        });

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expenseClickListener.onExpenseClicked(closedTripItem, closedTripItem.getExpenseimage());

            }
        });

    }

    @Override
    public int getItemCount() {
        return closedTripItems.size();
    }

    class ClosedTripExpenseViewModel extends RecyclerView.ViewHolder {

        TextView textExpenseName, textAmount, textView;
        MaterialCardView parentCardView;
        ImageView imgProfile;

        public ClosedTripExpenseViewModel(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            textExpenseName = itemView.findViewById(R.id.textExpenseName);
            textAmount = itemView.findViewById(R.id.textAmount);
            textView = itemView.findViewById(R.id.textView);


            parentCardView = itemView.findViewById(R.id.parentCardView);

        }
    }

    interface ExpenseClickListener {
        void onExpenseClicked(ClosedTripExpenseItem closedTripExpenseItem, String imgPath);
    }

}
