package com.app.wheelsonadminapp.ui.home.service;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.service.ServiceItem;

import java.util.List;

public class ListDataAdapter extends RecyclerView.Adapter<ListDataAdapter.FruitViewHolder>  {
    private List<ServiceItem> mDataset;
    RecyclerViewItemClickListener recyclerViewItemClickListener;

    public ListDataAdapter(List<ServiceItem> mDataset, RecyclerViewItemClickListener listener) {
        this.mDataset = mDataset;
        this.recyclerViewItemClickListener = listener;
    }

    @NonNull
    @Override
    public FruitViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row, parent, false);

        FruitViewHolder vh = new FruitViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(@NonNull FruitViewHolder fruitViewHolder, int i) {
        fruitViewHolder.mTextView.setText(mDataset.get(i).getServicename());
        Log.i("NEW", "onBindViewHolder: "+mDataset.get(i).toString());
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public  class FruitViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;

        public FruitViewHolder(View v) {
            super(v);
            mTextView = (TextView) v.findViewById(R.id.textView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            recyclerViewItemClickListener.clickOnItem(mDataset.get(this.getAdapterPosition()));

        }
    }

    public interface RecyclerViewItemClickListener {
        void clickOnItem(ServiceItem data);
    }
}
