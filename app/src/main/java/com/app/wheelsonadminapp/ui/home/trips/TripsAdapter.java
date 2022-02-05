package com.app.wheelsonadminapp.ui.home.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.wheelsonadminapp.R;
import com.app.wheelsonadminapp.model.trip.TripItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewModel> {

    List<TripItem>tripItems;
    Context mContext;
    TripClickListener tripClickListener;

    public TripsAdapter(List<TripItem>tripItems, Context mContext, TripClickListener tripClickListener) {
        this.tripItems = tripItems;
        this.mContext = mContext;
        this.tripClickListener = tripClickListener;
    }

    @NonNull
    @Override
    public TripViewModel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trips_recycler_row, parent, false);
        return new TripViewModel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewModel holder, int position) {
        TripItem tripItem = tripItems.get(position);
        holder.textTripFromTo.setText(tripItem.getFromlocation() +" to "+tripItem.getTolocation());
        holder.textStartDate.setText("Start Date : "+tripItem.getStartdate());
        holder.textEndDate.setText("End Date : "+tripItem.getEnddate());
        if(tripItem.getDriverName()!=null){
            holder.textDriverName.setText("Driver : "+tripItem.getDriverName());
        }else {
            holder.textDriverName.setText("Driver : "+"Not Assigned");
        }

        holder.textVehicleName.setVisibility(View.INVISIBLE);
        holder.textAmount.setText("Rs."+tripItem.getAmount());
        holder.parentCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripClickListener.OnTripClicked(tripItem);
            }
        });
        holder.textCheckStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tripClickListener.OnTripClicked(tripItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(tripItems==null){
            return 0;
        }else {
            return tripItems.size();
        }
    }

    class TripViewModel  extends RecyclerView.ViewHolder{

        TextView textTripFromTo,textStartDate,textEndDate,textDriverName,textVehicleName,textAmount,textCheckStatus;
        MaterialCardView parentCardView;

        public TripViewModel(@NonNull View itemView) {
            super(itemView);
            textTripFromTo = itemView.findViewById(R.id.textTripFromTo);
            textStartDate = itemView.findViewById(R.id.textStartDate);
            textEndDate = itemView.findViewById(R.id.textEndDate);
            textDriverName = itemView.findViewById(R.id.textDriverName);
            textVehicleName = itemView.findViewById(R.id.textVehicleName);
            textAmount = itemView.findViewById(R.id.textAmount);
            textCheckStatus = itemView.findViewById(R.id.textCheckStatus);
            parentCardView = itemView.findViewById(R.id.parentCardView);
        }
    }

    interface TripClickListener{
        void OnTripClicked(TripItem tripItem);
    }
}
